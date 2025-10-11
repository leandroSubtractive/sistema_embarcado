@file:Suppress("UnstableApiUsage")

import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
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
    kotlinOptions { jvmTarget = "11" }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.verticalseekbar)

    // Testes unitários
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.mockito.core)
    testImplementation(libs.mockito.android)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // Android Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.withType<Test> { useJUnitPlatform() }

// ----------------------
// Task custom para testes nativos
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

    // --- Utilitários de log ---
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

        // Detecta ABI do dispositivo
        val abiOutput = ByteArrayOutputStream()
        execOps.exec {
            commandLine("adb", "shell", "getprop", "ro.product.cpu.abi")
            standardOutput = abiOutput
        }
        val deviceAbi = abiOutput.toString().trim()

        // Seleciona ABI disponível no build
        val abi = if (buildDir.resolve(deviceAbi).exists()) deviceAbi
        else abiList.firstOrNull { buildDir.resolve(it).exists() }
            ?: error("Nenhum binário compatível encontrado no build directory")

        val abiDir = buildDir.resolve(abi)
        val binaryPath = abiDir.resolve(testBinaryName.get())
        val sharedLibPath = abiDir.resolve("libc++_shared.so")
        val deviceDir = deviceTestDir.get()
        val reportsPath = reportsDir.get().asFile.also { it.mkdirs() }
        val reportFileName = "report.xml"

        logInfo("ABI detectada: $abi")
        logInfo("Binário de teste: $binaryPath")
        logInfo("Biblioteca compartilhada: $sharedLibPath")
        logInfo("Diretório de teste no dispositivo: $deviceDir")

        fun runCmd(vararg args: String) = logStep(args.joinToString(" ")).also {
            execOps.exec { commandLine(*args) }
        }

        try {
            // Cria diretório e envia arquivos
            runCmd("adb", "shell", "mkdir", "-p", deviceDir)
            runCmd("adb", "push", binaryPath.absolutePath, deviceDir)
            runCmd("adb", "push", sharedLibPath.absolutePath, deviceDir)
            runCmd("adb", "shell", "chmod", "777", "$deviceDir/${testBinaryName.get()}")

            // Executa o binário no dispositivo
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

            // Puxa relatório de volta para o host
            runCmd("adb", "pull", "$deviceDir/$reportFileName", reportsPath.absolutePath)

            logSection("NATIVE TEST OUTPUT 📄")
            println(output.toString(Charsets.UTF_8))
            logSection("REPORT SAVED ✅")
            logInfo("Relatório salvo em: ${reportsPath.resolve(reportFileName).absolutePath}")

        } catch (e: Exception) {
            logError("Erro ao executar testes nativos: ${e.message}")
            throw e
        }
    }
}

// Registro da task
tasks.register<RunNativeTestsTask>("runNativeTests") {
    group = "verification"
    description = "Executa testes nativos C++ no dispositivo Android"

    nativeBuildDir.set(layout.buildDirectory.dir("intermediates/cmake/debug/obj"))
    deviceTestDir.set("/data/local/tmp/gtests/${project.name}")
    testBinaryName.set("vehicleequalizer_native_lib_tests")
    reportsDir.set(layout.buildDirectory.dir("test-results/native"))

    dependsOn("externalNativeBuildDebug")
    outputs.upToDateWhen { false }
}
