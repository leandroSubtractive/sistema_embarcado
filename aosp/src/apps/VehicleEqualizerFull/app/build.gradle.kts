@file:Suppress("UnstableApiUsage")

import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    //id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}

android {
    namespace = "com.leandromendes.vehicleequalizer"
    compileSdk = 36

    defaultConfig {
        applicationId = namespace
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                // Garante que a STL será compartilhada
                arguments += listOf("-DANDROID_STL=c++_shared")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

val junitBOM = "5.10.0" // Define BOM version (use the same version)

dependencies {
    // ----------------------------------------
    // Módulos de Aplicação (App Modules)
    // ----------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    // Media3
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.exoplayer)

    // Room and Lifecycle
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    ksp(libs.androidx.room.compiler)

    // Utility
    implementation(libs.verticalseekbar)

    // ----------------------------------------
    // Testes Unitários Locais (src/test) - JUnit 5 + Mockito/MockK
    // ----------------------------------------

    // Simplifica a configuração do JUnit 5 usando apenas o BOM para gerenciar versões
    testImplementation(platform("org.junit:junit-bom:$junitBOM"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    // O bundle jupiter-junit-jupiter é redundante se você usa os módulos acima. Removido.
    // jupiter.junit.jupiter removido.

    // Usamos 'testImplementation' para o engine, que o BOM deve resolver.
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    // Arquitetura e Coroutines
    // Consolida múltiplas adições de core-testing para a última versão
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // Ferramentas de Mocking: Foco no Mockito para AndroidX/JUnit 5.
    // É recomendado remover io.mockk:mockk:1.14.5 para evitar conflito de libs.
    testImplementation("org.mockito:mockito-core:5.11.0") // Versão estável mais recente (ajustada para ser mais recente que a sua anterior)

    // ----------------------------------------
    // Testes Instrumentados (src/androidTest) - AndroidX + Mockito-Kotlin
    // ----------------------------------------

    // AndroidX Test Core (para ServiceScenario e outras APIs)
    // Core and Core-Ktx consolidados para a versão mais recente
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0") // Você tinha libs.androidx.rules (implementation), mas aqui é o lugar certo para o teste

    // JUnit e Espresso
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Mockito para testes instrumentados
    // O mockito-android e mockito-kotlin são necessários para instrumented tests
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    androidTestImplementation("org.mockito:mockito-android:5.11.0") // Mantendo a versão coerente com mockito-core
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}


// ----------------------
// Custom task for native testing
// ----------------------
abstract class RunNativeTestsTask @Inject constructor(
    private val execOps: ExecOperations
) : DefaultTask() {

    @get:InputDirectory
    abstract val nativeBuildDir: DirectoryProperty

    @get:Input
    abstract val deviceTestDir: Property<String>

    @get:Input
    abstract val testBinaryName: Property<String>

    @get:OutputDirectory
    abstract val reportsDir: DirectoryProperty

    // Log Section
    private fun logSection(title: String) {
        val line = "=".repeat(60)
        println("\u001B[34m$line\n$title\n$line\u001B[0m")
    }

    private fun logStep(msg: String) = println("\u001B[32m➡️  $msg\u001B[0m")
    private fun logInfo(msg: String) = println("\u001B[36mℹ️  $msg\u001B[0m")
    private fun logError(msg: String) = println("\u001B[31m❌ $msg\u001B[0m")

    @TaskAction
    fun run() {
        logSection("RUNNING NATIVE TESTS 🚀")

        val buildDir = nativeBuildDir.get().asFile
        val abiList = listOf("x86_64", "arm64-v8a", "armeabi-v7a")

        // Detects device ABI
        val abiOutput = ByteArrayOutputStream()
        execOps.exec {
            commandLine("adb", "shell", "getprop", "ro.product.cpu.abi")
            standardOutput = abiOutput
        }
        val deviceAbi = abiOutput.toString().trim()

        // Select ABI available in the build
        val abi = if (buildDir.resolve(deviceAbi).exists()) deviceAbi
        else abiList.firstOrNull { buildDir.resolve(it).exists() }
            ?: error("No compatible binary found in the build directory")

        val abiDir = buildDir.resolve(abi)
        val binaryPath = abiDir.resolve(testBinaryName.get())
        val sharedLibPath = abiDir.resolve("libc++_shared.so")
        val deviceDir = deviceTestDir.get()
        val reportsPath = reportsDir.get().asFile.also { it.mkdirs() }
        val reportFileName = "report.xml"

        logInfo("ABI detected: $abi")
        logInfo("Biblioteca compartilhada: $binaryPath")
        logInfo("Shared library: $sharedLibPath")
        logInfo("Test directory on the device: $deviceDir")

        fun runCmd(vararg args: String) = logStep(args.joinToString(" ")).also {
            execOps.exec { commandLine(*args) }
        }

        try {
            // Runs the binary on the device
            runCmd("adb", "shell", "mkdir", "-p", deviceDir)
            runCmd("adb", "push", binaryPath.absolutePath, deviceDir)
            runCmd("adb", "push", sharedLibPath.absolutePath, deviceDir)
            runCmd("adb", "shell", "chmod", "777", "$deviceDir/${testBinaryName.get()}")

            // Runs the binary on the device
            val output = ByteArrayOutputStream()
            execOps.exec {
                commandLine(
                    "adb", "shell",
                    "LD_LIBRARY_PATH=$deviceDir",
                    "$deviceDir/${testBinaryName.get()}",
                    "--gtest_output=xml:$deviceDir/$reportFileName"
                )
                standardOutput = output
                errorOutput = output
                isIgnoreExitValue = true
            }

            // Pulls report back to host
            runCmd("adb", "pull", "$deviceDir/$reportFileName", reportsPath.absolutePath)

            logSection("NATIVE TEST OUTPUT 📄")
            println(output.toString(Charsets.UTF_8))
            logSection("REPORT SAVED ✅")
            logInfo("Report saved in: ${reportsPath.resolve(reportFileName).absolutePath}")

        } catch (e: Exception) {
            logError("Error running native tests: ${e.message}")
            throw e
        }
    }
}

// Task registration
tasks.register<RunNativeTestsTask>("runNativeTests") {
    group = "verification"
    description = "“Runs native C++ tests on the Android device”"

    nativeBuildDir.set(layout.buildDirectory.dir("intermediates/cmake/debug/obj"))
    deviceTestDir.set("/data/local/tmp/gtests/${project.name}")
    testBinaryName.set("vehicleequalizer_native_lib_tests")
    reportsDir.set(layout.buildDirectory.dir("test-results/native"))

    dependsOn("externalNativeBuildDebug")
    outputs.upToDateWhen { false }
}
