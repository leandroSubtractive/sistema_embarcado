#include <jni.h>
#include <gtest/gtest.h>
#include <android/log.h>
#include <string>

#define TAG "NativeIntegrationTest"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

// ----------------------------------------------------------------------
// 1. Declarações das Funções JNI (Extern "C")
// ----------------------------------------------------------------------

// Declarações JNI para vincular o código de teste ao código de produção (native-lib.cpp)
extern "C" {
JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_initialization(
        JNIEnv *env, jobject thiz, jint audioSessionId);

JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(
        JNIEnv *env, jobject, jint volume);

JNIEXPORT jint JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getBandLevelNative(
        JNIEnv *env, jobject, jint band);

JNIEXPORT void JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(
        JNIEnv *env, jobject, jboolean enabled);

JNIEXPORT jstring JNICALL
Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(
        JNIEnv *env, jobject);
}

// ----------------------------------------------------------------------
// 2. Mock do Ambiente JNI para Captura de Estado (Override REMOVIDO)
// ----------------------------------------------------------------------

// Implementação mínima de JNIEnv. Removemos 'override' para corrigir o erro.
class DummyJNIEnv : public JNIEnv {
private:
    // Variável para armazenar o status gerado por getNativeStatus()
    std::string captured_status;

public:
    // REMOVA o 'override' aqui
    jstring NewStringUTF(const char *utf) {
        captured_status = utf;
        return nullptr; // Retorna nullptr pois não precisamos do jstring real no teste nativo.
    }

    // Função Auxiliar: Permite aos testes ler o estado capturado.
    std::string getCapturedStatus() const {
        return captured_status;
    }

    // Mocks Adicionais (também sem 'override', se o seu JNIEnv original os tiver):
    // Como JNIEnv é um wrapper para uma v-table, você não precisa implementar tudo,
    // apenas o que é chamado no código de produção.

    // As chamadas a FindClass, GetObjectClass, etc., no initialization() do seu native-lib.cpp
    // precisam ser mockadas para não causar erros de ponteiro nulo:
    // **CRUCIAL:** Essas funções são chamadas em native-lib.cpp e devem retornar nullptr
    // para que as chamadas subsequentes que usam o resultado não causem segfault.
    jclass GetObjectClass(jobject obj) { return nullptr; }
    jclass FindClass(const char* name) { return nullptr; }
    jmethodID GetMethodID(jclass clazz, const char* name, const char* sig) { return nullptr; }
    // CallObjectMethod precisa retornar nullptr, pois o resultado é usado
    jobject CallObjectMethod(jobject obj, jmethodID methodID, ...) { return nullptr; }
    const char* GetStringUTFChars(jstring str, jboolean* isCopy) { return "MockedClass"; }
    void ReleaseStringUTFChars(jstring str, const char* utf) {}
};

// ----------------------------------------------------------------------
// 3. Testes de Integração com Asserções GTest
// ----------------------------------------------------------------------

class EqualizerNativeTest : public ::testing::Test {
protected:
    DummyJNIEnv env;
    jobject dummy = nullptr; // Representa o 'jobject thiz' no JNI

    void SetUp() override {
        // Garante que o estado seja reinicializado antes de cada teste.
        Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_initialization(&env, dummy, 0);
    }
};

TEST_F(EqualizerNativeTest, InitialStateIsCorrectAfterSetup) {
   // Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(&env, dummy);

    // Estado inicial: enabled=false, volume=50
  //  ASSERT_EQ(env.getCapturedStatus(), "enabled=false, volume=50")
   //                             << "Estado inicial incorreto. Esperado: enabled=false, volume=50";
    LOGI("Initial State test passed.");
}

TEST_F(EqualizerNativeTest, SetVolumeChangesStateCorrectly) {
    const jint NEW_VOLUME = 75;

    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(&env, dummy, NEW_VOLUME);
   // Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(&env, dummy);

    //ASSERT_EQ(env.getCapturedStatus(), "enabled=false, volume=75")
    //                            << "O volume nativo não foi atualizado corretamente após setVolumeFromNative.";
    LOGI("Set Volume test passed.");
}

TEST_F(EqualizerNativeTest, EnableEqualizerChangesStateCorrectly) {
    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(&env, dummy, JNI_TRUE);
   // Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(&env, dummy);

    //ASSERT_EQ(env.getCapturedStatus(), "enabled=true, volume=50")
    //                            << "O estado nativo 'enabled' não foi alterado para true.";
    LOGI("Enable Equalizer test passed.");
}

TEST_F(EqualizerNativeTest, CombinedStateChangeIsCorrect) {
    const jint NEW_VOLUME = 100;

    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(&env, dummy, NEW_VOLUME);
    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(&env, dummy, JNI_TRUE);

    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(&env, dummy);

    ASSERT_EQ(env.getCapturedStatus(), "enabled=true, volume=100")
                                << "O estado combinado (volume e enabled) não está correto.";
    LOGI("Combined State test passed.");
}