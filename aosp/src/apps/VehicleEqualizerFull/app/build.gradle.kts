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
        applicationId = "com.leandromendes.vehicleequalizer"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                // Ative testes
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
    kotlinOptions {
        jvmTarget = "11"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    // Define onde o executável de teste será instalado no dispositivo/emulador
    // Usamos 'val' (Kotlin) em vez de 'def' (Groovy)
    val deviceTestDir = "/data/local/tmp/gtests/${project.name}"
    val testBinaryName = "vehicleequalizer_native_lib_tests"

    tasks.register<Exec>("runNativeTests") {
        group = "verification"
        description = "Runs C++ GTests on the connected Android device/emulator."

        val abi = "x86_64" // Mantenha a ABI que você confirmou para seu emulador (x86_64)

        // 1. Caminho para o executável de teste compilado
        val binaryPath = project.layout.buildDirectory.dir("intermediates/cmake/debug/obj").get().asFile.absolutePath
        val finalBinaryPath = "$binaryPath/$abi/$testBinaryName"

        // 2. CORREÇÃO CRÍTICA: Caminho padrão para libc++_shared.so dentro da pasta 'sources' do NDK.
        val ndkPath = project.android.ndkDirectory.absolutePath

        // Este é o caminho mais comum para a libc++_shared.so no NDK moderno.
        // Ele usa o android.ndkDirectory para encontrar a biblioteca de runtime.
        val sharedLibPath = "$ndkPath/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/lib/x86_64-linux-android/libc++_shared.so"

        // Alternativamente, se o caminho acima falhar (versões mais antigas do NDK):
        // val sharedLibPath = "$ndkPath/sources/cxx-stl/llvm-libc++/libs/$abi/libc++_shared.so"

        val sharedLibFileName = "libc++_shared.so"

        commandLine("sh", "-c", """
        # 1. Cria o diretório no dispositivo
        adb shell mkdir -p $deviceTestDir && \
        
        # 2. Faz PUSH do EXECUTÁVEL
        adb push $finalBinaryPath $deviceTestDir && \
        
        # 3. Faz PUSH da BIBLIOTECA COMPARTILHADA
        adb push $sharedLibPath $deviceTestDir && \
        
        # 4. Garante permissão de execução
        adb shell chmod 777 $deviceTestDir/$testBinaryName && \
        
        # 5. Define LD_LIBRARY_PATH e executa o GTest
        adb shell "export LD_LIBRARY_PATH=$deviceTestDir && $deviceTestDir/$testBinaryName"
    """.trimIndent())
    }

    // 3. Adiciona a dependência: o teste deve rodar após a compilação C++ estar pronta
    tasks.named("runNativeTests") {
        // Garante que o executável C++ seja compilado antes de tentar executá-lo
        dependsOn("externalNativeBuildDebug")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.core.ktx)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    testImplementation(libs.junit)
    testImplementation(libs.mockk.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    implementation(libs.androidx.recyclerview)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media)
    implementation(libs.androidx.appcompat.v130)
    implementation(libs.androidx.core.ktx.v1170)

    implementation(libs.verticalseekbar)

    testImplementation(libs.mockito.mockito.core)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockito.android)

}

tasks.withType<Test> {
    useJUnitPlatform()
}