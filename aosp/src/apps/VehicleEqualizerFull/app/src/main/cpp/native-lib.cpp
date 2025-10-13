#include <jni.h>
#include <android/log.h>
#include <string>

// Setting TAGs for Logcat
#define TAG_NATIVE_AUDIO "NativeAudioProcessor"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG_NATIVE_AUDIO, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  TAG_NATIVE_AUDIO, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG_NATIVE_AUDIO, __VA_ARGS__)

#define NUM_BANDS 5

typedef struct {
    int audioSessionId;
    int dB;
    char frequency[10];
} band;

static bool s_equalizerEnabled = false;
static int s_volumeLevel = 50;
static band s_bandFrequency[NUM_BANDS];
static char bandNames[NUM_BANDS][10] = {"60Hz", "230Hz", "910Hz", "3.6kHz", "14kHz"};

extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_initialization(
        JNIEnv *env, jobject thiz, jint audioSessionId) {

    // Get the class of the object that called it (thiz)
    jclass cls = env->GetObjectClass(thiz);

    // Get the name of the Java class
    jclass classClass = env->FindClass("java/lang/Class");
    jmethodID getName = env->GetMethodID(classClass, "getName", "()Ljava/lang/String;");
    auto name = (jstring) env->CallObjectMethod(cls, getName);

    const char *className = env->GetStringUTFChars(name, nullptr);
    LOGD("Calling class: %s", className);
    env->ReleaseStringUTFChars(name, className);

    for (int i = 0; i < NUM_BANDS; i++) {
        s_bandFrequency[i].dB = 0;
        strncpy(s_bandFrequency[i].frequency, bandNames[i], sizeof(bandNames[i]));
        s_bandFrequency[i].audioSessionId = audioSessionId;
    }
    LOGI("audioSessionId: %d", audioSessionId);
}

// JNI function to set the volume level
extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(
        JNIEnv *env, jobject /* this */, jint volume) {
    s_volumeLevel = volume;
    LOGI("Volume: %d", s_volumeLevel);
}

// JNI function to set the gain for each frequency band
extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setBandLevelNative(
        JNIEnv *env, jobject /* this */, jint band, jint level) {
    if (band < 0 || band > 4) {
        LOGE("Frequency band %d does not exist", band);
    } else {
        LOGD("Band ID:[%d]", band);

        s_bandFrequency[band].dB = level;
        LOGI("Band %s gain %ddB", s_bandFrequency[band].frequency, s_bandFrequency[band].dB);
    }

}


extern "C" JNIEXPORT jint JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getBandLevelNative(
        JNIEnv *env, jobject /* this */, jint band) {

    if (band < 0 || band > 4) {
        LOGE("Frequency band %d does not exist", band);
    } else {
        LOGD("Band ID:[%d]", band);
        LOGI("Band %s gain %ddB", s_bandFrequency[band].frequency, s_bandFrequency[band].dB);
        return s_bandFrequency[band].dB;
    }
    return 0;
}

// JNI function to enable/disable the equalizer (Emulated)
extern "C" JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(
        JNIEnv *env, jobject /* this */, jboolean enabled) {

    // Update equalizer status
    s_equalizerEnabled = (enabled == JNI_TRUE);
    LOGI("Native equalizer %s", s_equalizerEnabled ? "activated" : "deactivated");
}

// Auxiliary function for status query — used by integration tests
extern "C" JNIEXPORT jstring JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(
        JNIEnv *env, jobject thiz) {
    std::string status = "enabled=" + std::string(s_equalizerEnabled ? "true" : "false") +
                         ", volume=" + std::to_string(s_volumeLevel);
    return env->NewStringUTF(status.c_str());
}
