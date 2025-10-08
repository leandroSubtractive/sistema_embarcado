# Desenvolvimento de serviço nativo Android com o uso de classes de serviços

---

## CURSO

FORMAÇÃO EM SISTEMAS EMBARCADOS

LEANDRO MENDES DOS SANTOS

---

## Links

Link do Reposítorio: [gitHub](https://github.com/leandroSubtractive/sistema_embarcado/)

Link da Aplicação: [VehicleEqualizerApp](https://github.com/leandroSubtractive/sistema_embarcado/tree/devel/aosp/src/apps/VehicleEqualizerFull)

Link do Vídeo: [Vídeo](https://drive.google.com/drive/folders/1xURd7VnulzM-5QFO18Kk7mN_jLk9DK0C)

---

## OBJETIVOS DE APRENDIZAGEM

- Implementar métodos do ciclo de vida dos serviços de áudio no Android;
- Elaborar um serviço de reprodução de áudio no Android;
- Implementar a chamada do serviço de reprodução de áudio no Android.

---

## Introdução

Esté documento descreve a implementação e as funcionalidades do aplicativo de equalização. Sua integração com o AudioService e os modulos de Notificação, PlayBack e Equalização.

---

1. [Arquitetura](#1-arquitetura)
    - 1.1. [Estrutura de Pastas](#11-estrutura-de-pastas)
    - 1.2. [Módulos](#12-módulos)
      - 1.2.1. [Módulo de Reprodução (PlaybackModule)](#121-módulo-de-reprodução-playbackmodule)
      - 1.2.2. [Módulo de Equalização (EqualizationModule)](#122-módulo-de-equalização-equalizationmodule)
        - 1.2.2.1. [Módulo de Equalização Com JNI](#1221-módulo-de-equalização-com-jni)
      - 1.2.3. [Módulo de Notificação (NotificationModule)](#123-módulo-de-notificação-notificationmodule)
    - 1.3. [Serviço](#13-serviço)
    - 1.4. [Permissões](#14-permissões)
2. [Definição dos Requisitos Funcionais](#2-definição-dos-requisitos-funcionais)
    - 2.1. [Reprodução](#21-reprodução)
    - 2.2. [Equalização](#22-equalização)
    - 2.3. [Notificação](#23-notificação)
3. [Referências](#3-referências)

---

---

## 1. Arquitetura

### 1.1 Estrutura de Pastas

Essa é a estrutura de pastas do projeto, vamos dar ênfase ao conteúdo de `modules` e `service`.

```sh
.
├── data
│   ├── AppDatabase.kt
│   ├── dao
│   │   └── ProfileDao.kt
│   ├── model
│   │   └── EqualizerProfile.kt
│   └── repository
│       └── UserRepository.kt
├── modules # Módulos com as funcionalidades de áudio
│   ├── equalizer # Responsável pela equalização do áudio
│   │   ├── EqualizerInterface.kt
│   │   └── EqualizerModule.kt
│   ├── notification # Responsável pelo gerenciamento de notificações
│   │   ├── NotificationInterface.kt
│   │   └── NotificationModule.kt
│   └── playback # Responsável pelo reprodutor de mídias
│       ├── PlaybackInterface.kt
│       └── PlaybackModule.kt
├── ProfileApplication.kt
├── service # Responsável por gerenciar serviço de áudio
│   └── AudioService.kt
├── ui
│   ├── EqualizerActivity.kt
│   ├── MainActivity.kt
│   └── viewmodel
│       ├── MainViewModelFactory.kt
│       └── MainViewModel.kt
└── util
    ├── Constants.kt
    └── ProfileRecyclerViewAdapter.kt
```

### 1.2 Módulos

#### 1.2.1 Módulo de Reprodução (PlaybackModule)

O Playback Module é responsável pelo gerenciamento da execução de arquivos de áudio, abaixo, a interface (`PlaybackInterface.kts`) desenvolvida com todos os métodos utilizados para o correto funcionamento do serviço de áudio.
Foi utilizado o `android.media.MediaPlayer`[[1]](https://developer.android.com/reference/android/media/MediaPlayer) na classe de módulo responsável por executar os comandos básicos de execução.

```kotlin
package com.leandromendes.vehicleequalizer.modules.playback

/**
 * Playback interface
 * Interface with all methods of the playbackModule class
 *
 * @constructor Create empty Playback interface
 */
interface PlaybackInterface {
    /**
     * Set on prepared listener
     * Register a listener that will be called when the media player is ready
     *
     * @param listener Callback
     */
    fun setOnPreparedListener(listener: () -> Unit)

    /**
     * Set raw data source
     *
     * @param resId Audio resource ID
     */
    fun setRawDataSource(resId: Int)

    /**
     * Play
     * Start playing the audio track
     */
    fun play()

    /**
     * Pause
     * Pause audio playback
     */
    fun pause()

    /**
     * Stop
     * Stop playing the audio track
     */
    fun stop()

    /**
     * Seek to
     * Changes the position of the audio track based on the position of the SeekBar
     *
     * @param position SeekBar position
     */
    fun seekTo(position: Int)

    /**
     * Get duration
     *
     * @return Get the duration of the loaded audio track
     */
    fun getDuration(): Int

    /**
     * Get current position
     *
     * @return Get the current position of the loaded audio track
     */
    fun getCurrentPosition(): Int

    /**
     * Is playing
     *
     * @return Returns the status of the music player
     */
    fun isPlaying(): Boolean

    /**
     * Get audio session id
     *
     * @return Returns the ID of the audio section
     */
    fun getAudioSessionId(): Int

    /**
     * Release
     * Release player resource
     */
    fun release()
}

```

O método `setRawDataSource()` é responsável por criar a seção de áudio e carregar o arquivo de áudio no player. Uma callback é chamada assim que a seção está preparada, essa callback é responsável por inicializar o módulo de equalização.

```kotlin
    override fun setRawDataSource(@RawRes resId: Int) {
        release()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            setOnPreparedListener {
                Log.d(logTAG, "MediaPlayer prepared")
                onPreparedListener?.invoke()
            }
            setOnCompletionListener {
                Log.d(logTAG, "Finished track.")
            }
        }
    }
```

#### 1.2.2 Módulo de Equalização (EqualizationModule)

O módulo de equalização é responsável por realizar o processamento do áudio, aplicando alteração de ganho em respectivas frequências de atuação.

A interface `EqualizerInterface()` contém todos os métodos implementados no módulo. Para realizar equalização real no arquivo de áudio, foi utilizado o 
`android.media.audiofx.Equalizer`[[2]]([Equalizer](https://developer.android.com/reference/android/media/audiofx/Equalizer)).

```kotlin
package com.leandromendes.vehicleequalizer.modules.equalizer

import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

interface EqualizerInterface {

    /**
     * Apply profile
     *
     * @param profile Audio Profile
     */
    fun applyProfile(profile: EqualizerProfile)

    /**
     * Set volume
     *
     * @param level Volume value in percent [0 - 100]
     */
    fun setVolume(level: Int)

    /**
     * Set band level safe
     *
     * @param band Band ID [0 - 4]
     * @param level Gain in dB [-15 - 15]
     */
    fun setBandLevelSafe(band: Int, level: Int)

    /**
     * Set enable
     *
     * @param enabled Equalizer Status
     */
    fun setEnable(enabled: Boolean)

    /**
     * Get enabled
     *
     * @return Current status
     */
    fun getEnabled(): Boolean

    /**
     * Reset
     * Reset to default values 0db
     */
    fun reset()

    /**
     * Release
     * Enable equalizer feature
     */
    fun release()

    /**
     * Print bands info
     * Displays all equalizer information on the terminal, used for Log
     */
    fun printBandsInfo()
}
```

Os valores de limite de ajuste das barras do equalizador estão configurados direto na SeekBar. Para descobrir esses limites, implementei os métodos. 
`printBandsInfo()`[[3]](https://developer.android.com/reference/android/media/audiofx/Equalizer#getCenterFreq(short)) que exibe a quantidade de bandas do equalizador, quais frequências para cada banda e os limites máximo e mínimo de ajustes.

```kotlin
override fun printBandsInfo() {
    val bands = equalizer?.numberOfBands ?: 0
    val range = equalizer?.bandLevelRange ?: shortArrayOf(0, 0)

    Log.d(logTAG, "Equalizer has $bands bands (Min=${range[0]} dB, Max=${range[1]} dB)")

    for (i in 0 until bands) {
        val centerFreq = equalizer?.getCenterFreq(i.toShort()) ?: 0
        Log.d(logTAG, "Band $i → Freq: ${centerFreq / 1000} Hz")
    }
}

// Saída da função:

// Equalizer has 5 bands (Min=-15dB, Max=15dB)
// Band 0 → Freq: 60 Hz
// Band 1 → Freq: 230 Hz
// Band 2 → Freq: 910 Hz
// Band 3 → Freq: 3600 Hz
// Band 4 → Freq: 14000 Hz
```

##### 1.2.2.1. Módulo de Equalização Com JNI

O primeiro passo para a implementação do JNI junto do projeto que estou desenvolvendo é configurar o projeto para ser capaz de compilar código-fonte em C++ .

Arquivos criados e adicionados ao projeto:

- `app/src/main/cpp/native-lib.cpp`: Este é o arquivo C++ onde é implementado o código nativo.

```c++
#include <jni.h>
#include <string>
#include <android/log.h>

// Setting TAGs for Logcat
#define TAG_NATIVE_AUDIO "NativeAudioProcessor"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG_NATIVE_AUDIO, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  TAG_NATIVE_AUDIO, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG_NATIVE_AUDIO, __VA_ARGS__)


static bool s_equalizerEnabled = false;
static int s_volumeLevel = 50;
static int s_bandLevel = 0;
static int s_bandId = 0;
static char s_bandFrequency[5][10] = {"60Hz", "230Hz", "910Hz", "3.6kHz", "14kHz"};

// JNI function to enable/disable the equalizer (Emulated)
extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(
        JNIEnv *env,
        jobject thiz,
        jboolean enabled) {

    // Get the class of the object that called it (thiz)
    jclass cls = env->GetObjectClass(thiz);

    // Get the name of the Java class
    jclass classClass = env->FindClass("java/lang/Class");
    jmethodID getName = env->GetMethodID(classClass, "getName", "()Ljava/lang/String;");
    jstring name = (jstring) env->CallObjectMethod(cls, getName);

    const char *className = env->GetStringUTFChars(name, nullptr);
    LOGD("Calling class: %s", className);
    env->ReleaseStringUTFChars(name, className);

    // Update equalizer status
    s_equalizerEnabled = (enabled == JNI_TRUE);
    LOGI("Native equalizer %s", s_equalizerEnabled ? "activated" : "deactivated");
}

// JNI function to set the gain for each frequency band
extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setBandLevelNative(
        JNIEnv *env, jobject /* this */, jint Band, jint level) {
    s_bandLevel = level;
    s_bandId = Band;
    if (s_bandId < 0 || s_bandId > 4) {
        LOGE("Frequency band %d does not exist", s_bandId);
    } else {
        LOGD("Band ID:[%d]", s_bandId);
        LOGI("Band %s gain %ddB", s_bandFrequency[s_bandId], s_bandLevel);
    }

}

// JNI function to set the volume level
extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(
        JNIEnv *env, jobject /* this */, jint volume) {
    s_volumeLevel = volume;
    LOGI("Volume: %d", s_volumeLevel);
}
```

- `app/CMakeLists.txt:` Este arquivo é usado pelo CMake para gerenciar a compilação do código C++.

```c++
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("vehicleequalizer")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.
add_library(${CMAKE_PROJECT_NAME} SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        native-lib.cpp)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(${CMAKE_PROJECT_NAME}
        # List libraries link to the target library
        android
        log)
```

- `app/build.gradle (Module: app):` Adequação do arquivo para incluir o NDK e o CMake.

```kotlin
android {
    ...
    kotlinOptions {
        jvmTarget = "11"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}
```

Após realizar o sync e compilar o projeto, o arquivo de biblioteca é gerado:

- `app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/x86_64/libvehicleequalizer.so`

O código nativo atuaria como uma ponte com o VHALL do Android, no meu caso, ela apenas loga os valores dos comandos para fins de demonstração.

Log output

```sh
Band ID:[0]
Band 60Hz gain 0dB
Band ID:[1]
Band 230Hz gain 0dB
Band ID:[2]
Band 910Hz gain 0dB
Band ID:[3]
Band 3.6kHz gain 0dB
Band ID:[4]
Band 14kHz gain 0dB
Volume: 11
```

```sh
Calling class: com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerModule
Native equalizer deactivated
Calling class: com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerModule
Native equalizer activated
```

As chamadas e a inicialização feitas dentro do módulo de equalização.

- `EqualizerModule.kt`

```kotlin
    override fun setVolume(level: Int) {
...

...
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
            setVolumeFromNative(newVolume)
...

...
    override fun setBandLevelSafe(band: Int, level: Int) {
        val eq = equalizer ?: return
        if (!enabled) return
...

...
            eq.setBandLevel(band.toShort(), ((safeLevel * 100).toShort()))
            setBandLevelNative(band, safeLevel.toInt())
...

...
    override fun setEnable(enabled: Boolean) {
        this.enabled = enabled
        equalizer?.enabled = enabled
        setEqualizerEnabledNative(enabled) // Native Call
...
...
    /**
     * A native method that is implemented by the 'vehicleequalizer' native library,
     * which is packaged with this application.
     */
    external fun setEqualizerEnabledNative( enabled: Boolean)
    external fun setBandLevelNative(band: Int, level: Int)
    external fun setVolumeFromNative(volume: Int)


    companion object {
        // Used to load the 'vehicleequalizer' library on application startup.
        init {
            System.loadLibrary("vehicleequalizer")
        }
    }
}
```

#### 1.2.3 Módulo de Notificação (NotificationModule)

Módulo responsável pela criação e gerenciamento da notificação persistente na barra de status. A interface `NotificationInterface()` exponha os métodos implementados no módulo de notificação.

```kotlin
package com.leandromendes.vehicleequalizer.modules.notification

import android.app.Notification

/**
 * Notification interface
 *
 * @constructor Create empty Notification interface
 */
interface NotificationInterface {

    /**
     * Build notification
     *
     * @param playbackState Current status of the player
     * @param trackTitle Audio track name
     * @return Notification class object
     */
    fun buildNotification(playbackState: String, trackTitle: String = "No music"): Notification

    /**
     * Show notification
     *
     * @param playbackState Current status of the player
     * @param trackTitle Audio track name
     */
    fun showNotification(playbackState: String, trackTitle: String = "No music")

    /**
     * Update notification
     *
     * @param playbackState Current status of the player
     * @param trackTitle Audio track name
     */
    fun updateNotification(playbackState: String, trackTitle: String = "No music")

    /**
     * Cancel notification
     *
     */
    fun cancelNotification()
}

```

### 1.3 Serviço

A classe `AudioService()` que estende o `android.app.Service`[[4]](https://developer.android.com/reference/android/app/Service) do Android é responsável por inicializar e controlar todos os módulos citados acima, além disso, o AudioService publica atualizações para a interface do aplicativo. Vou descrever de maneira objetiva os principais métodos da classe.

O serviço é inicializado em primeiro plano[[5]](https://developer.android.com/develop/background-work/services/fgs) na `MainActivity` conforme abaixo:

```kotlin
  // starts AudioService right at the beginning
  val serviceIntent = Intent(this, AudioService::class.java)
  startForegroundService(serviceIntent)
```

No método onCreate do AudioService é onde todos os módulos são iniciados.

Os módulos de notificação e playback são inicializados em sequência, já o módulo do equalizador é inicializado via CallBack do playback modulo. O motivo disso é que o módulo de equalização precisa do ID da seção de áudio criada no MediaPlayer para funcionar corretamente.

```kotlin
    override fun onCreate() {
        super.onCreate()
        Log.d(logTAG, "Service created")

        notificationModule = NotificationModule(this)
        playbackModule = PlaybackModule(this)

        // Only initialize the equalizer when MediaPlayer is ready
        if (playbackModule is PlaybackModule) {
            (playbackModule as PlaybackModule).setOnPreparedListener {
                initEqualizerIfNeeded()
            }
        }
```

Nessa seção ocorre o mapeamento dos arquivos de áudio de exemplo que serão utilizados no aplicativo.

```Kotlin
        trackList = listOf( // List of songs for testing
            Track(R.raw.toto_africa, "Toto Africa"),
            Track(R.raw.spain_yohan_kim_friends_concert_live, "Yohan Kim Friends")
        )
```

Após o mapeamento das músicas, elas são carregadas no player.

```Kotlin
        loadTrack(currentTrackIndex)
```

Por fim, duas coisas são realizadas: é iniciado o serviço de notificação com as informações e comandos do player e é iniciado o `Looper` que publica as informações para atualização das Activitys.

```Kotlin
        //  Start notification already in stopped state
        startForeground(
            NotificationModule.NOTIFICATION_ID,
            notificationModule.buildNotification(
                PlaybackStates.STOPPED,
                trackList[currentTrackIndex].title
            )
        )

        // Periodic updates (player position)
        updateRunnable = object : Runnable {
            override fun run() {
                broadcastState()
                if (playbackModule.isPlaying()) {
                    handler.removeCallbacks(this) // Avoids duplication
                    handler.postDelayed(this, 1000)
                }
            }
        }

    }
```

Informações publicadas periodicamente.

```kotlin
/**
 * Broadcast state
 * states that the service sends
 */
private fun broadcastState() {
    val intent = Intent(MusicConstants.BROADCAST_MUSIC_STATE).apply {
        // State of music playback
        putExtra(MusicConstants.EXTRA_STATE, playbackState())
        // Get the current position of the song to update the counter in the interface
        putExtra(MusicConstants.EXTRA_CURRENT_POSITION, playbackModule.getCurrentPosition())
        // obtains the total duration of the song
        putExtra(MusicConstants.EXTRA_DURATION, playbackModule.getDuration())
        // Current song title
        putExtra(MusicConstants.EXTRA_TRACK_TITLE, trackList[currentTrackIndex].title)
    }
    sendBroadcast(intent)
}
```

O valor de retorno escolhido foi `START_STICKY`[[6]](https://developer.android.com/reference/android/app/Service#START_STICKY), por garantir, caso o serviço não inicie, que ele seja reiniciado automaticamente, que no nosso caso é o ideal, pois garante consistência e todo o gerenciamento do serviço é realizado dentro da MainActivity.

```kotlin
override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val action = intent?.action
    ...
    // Processa as ações recebidas
    ...
    return START_STICKY
}
```

### 1.4 Permissões

Permissões necessarias:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
```

<p style="text-align:center">
    <img src=imgs/Screenshot_20251008_005625.png alt style="width:32%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 5:</strong> Tela de Permissão</figcaption>
</p>

Cadastro do serviço

```xml
<service
    android:name=".service.AudioService"
    android:foregroundServiceType="mediaPlayback"
    android:enabled="true"
    android:exported="false">
    <intent-filter>
        <action android:name="androidx.media3.session.MediaSessionService"/>
        <action android:name="android.media.browse.MediaBrowserService"/>
    </intent-filter>
</service>
```

## 2. Definição dos Requisitos Funcionais

Estas são as duas telas do aplicativo, a primeira, responsável por exibir os perfis e gerenciar o Media Play e a lista de músicas, a segunda, responsável pela equalização, contendo todos os controles do equalizador, incluindo um botão que desativa a equalização.

<div style="display: flex; justify-content: space-around;">
  <div style="width: 45%; text-align: center;">
    <img src=imgs/tela_de_perfis.png alt style="width: 100%; height: auto;"/>
    <p><strong>Figura 1:</strong> Tela de Perfis</p>
  </div>
  <div style="width: 45%; text-align: center;">
    <img src=imgs/tela_de_edição.png alt style="width: 100%; height: auto;"/>
    <p><strong>Figura 2:</strong> Tela de Edição do Equalizador</p>
  </div>
</div>

A interface foi atualizada também [Versão anterior](https://github.com/leandroSubtractive/sistema_embarcado/blob/devel/readme_files/DC2%5BHandsOn_UA3-UA4%5D%20Aplicativo%20Android%20que%20armazena%20e%20sincroniza%20perfis%20de%20equaliza%C3%A7%C3%A3o%20de%20som.md#2-mudan%C3%A7a-na-interface), para atender as especificações do projeto. Para a implementação das SeekBars na vertical foi utilizado a biblioteca [android-verticalseekbar
](https://github.com/h6ah4i/android-verticalseekbar)

Um novo layout foi implementado para rodar o Aplicativo em um infotainment do Android automotive. A principal alteração do layout foi no estilo da SeekBar e no tamanho das letras. Abaixo os arquivos de layout utilizados para o Android Automotive.

<p style="text-align:center">
    <img src=imgs/tela_de_perfis_car.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 3:</strong> Tela de Perfis Android Automotive</figcaption>
</p>

<p style="text-align:center">
    <img src=imgs/tela_de_edição_car.png alt style="width:100%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 4:</strong> Tela de Edição do Equalizador Android Automotive</figcaption>
</p>

Arquivos de layout modificados para a tela do infotainment.

```txt
res/drawable/ic_pause_.xml
res/drawable/ic_play_arrow_.xml
res/drawable/ic_skip_next_.xml
res/drawable/ic_skip_previous_.xml
res/layout/activity_equalizer_.xml
res/layout/activity_main_.xml
```

### 2.1 Reprodução

Todas as principais funcionalidades do reprodutor foram implementadas, conforme a imagem abaixo, com os itens selecionados em vermelho.

Métodos implementados:

- Play
- Pause
- Próximo (Seleção de faixa)
- Anterior (Seleção de faixa)
- Busca (seek) dentro da faixa de áudio
- Exibição da faixa correspondente

<p style="text-align:center">
    <img src=imgs/MediaPlay.png alt style="width:50%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 5:</strong> Seção do Player</figcaption>
</p>

### 2.2 Equalização

Na seção de equalização foram implementadas as seguintes funcionalidades:

- Equalização com 5 bandas de frequência
- Controle de volume
- On/Off do equalizador

<p style="text-align:center">
    <img src=imgs/Eq.png alt style="width:40%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 6:</strong> Seção do Equalizador</figcaption>
</p>

### 2.3 Notificação

As notificações são iniciadas logo que o aplicativo é aberto, para esta seção foram implementadas as seguintes funcionalidades:

- Play
- Pause
- Stop
- Exibe nome da música

<p style="text-align:center">
    <img src=imgs/Notification.png alt style="width:40%; height:auto;">
    <figcaption style="text-align:center"><strong>Figura 7:</strong> Seção de Notifição</figcaption>
</p>

## 3. Referências

1. [MediaPlayer](https://developer.android.com/reference/android/media/MediaPlayer)

2. [Equalizer](https://developer.android.com/reference/android/media/audiofx/Equalizer)
3. [getCenterFreq](https://developer.android.com/reference/android/media/audiofx/Equalizer#getCenterFreq(short))
4. [Service](https://developer.android.com/reference/android/app/Service)
5. [Foreground services overview](https://developer.android.com/develop/background-work/services/fgs)
6. [START_STICKY](https://developer.android.com/reference/android/app/Service#START_STICKY)
