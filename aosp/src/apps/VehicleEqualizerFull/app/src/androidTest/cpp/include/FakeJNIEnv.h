#pragma once
#include <jni.h>
#include <string>

// ----------------------
// Estado global do Equalizer
// ----------------------
struct EqualizerState {
    bool enabled = false;
    int volume = 50;
};

inline EqualizerState gEqualizerState;

// ----------------------
// Fake JNIEnv para testes
// ----------------------
struct FakeJNIEnv : public JNIEnv {
    std::string captured_status;

    jstring NewStringUTF(const char* utf) {
        captured_status = utf;
        return nullptr; // não precisamos do jstring real
    }

    const char* GetStringUTFChars(jstring str, jboolean* isCopy) {
        return captured_status.c_str();
    }

    void ReleaseStringUTFChars(jstring str, const char* chars) {
        // nada
    }

    jclass FindClass(const char* name) { return nullptr; }
    jclass GetObjectClass(jobject obj) { return nullptr; }
    jmethodID GetMethodID(jclass clazz, const char* name, const char* sig) { return nullptr; }
    jobject CallObjectMethod(jobject obj, jmethodID methodID, ...) { return nullptr; }
};

// ----------------------
// Funções JNI que serão chamadas pelos testes
// ----------------------
extern "C" {

JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_initialization(
        JNIEnv* env, jobject thiz, jint audioSessionId) {
    gEqualizerState.enabled = false;
    gEqualizerState.volume = 50;
}

JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(
        JNIEnv* env, jobject thiz, jint volume) {
    gEqualizerState.volume = volume;
}

JNIEXPORT jint JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getBandLevelNative(
        JNIEnv* env, jobject thiz, jint band) {
    return 0;
}

JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(
        JNIEnv* env, jobject thiz, jboolean enabled) {
    gEqualizerState.enabled = enabled;
}

JNIEXPORT jstring JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(
        JNIEnv* env, jobject thiz) {
    char buf[128];
    snprintf(buf, sizeof(buf), "enabled=%s, volume=%d",
             gEqualizerState.enabled ? "true" : "false",
             gEqualizerState.volume);
    static_cast<FakeJNIEnv*>(env)->captured_status = buf;
    return nullptr; // jstring real não é necessário nos testes
}

} // extern "C"
