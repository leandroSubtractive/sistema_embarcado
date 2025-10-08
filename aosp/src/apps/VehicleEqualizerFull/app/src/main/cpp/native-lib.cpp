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