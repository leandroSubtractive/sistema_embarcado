#include <gtest/gtest.h>
#include <android/log.h>
#include "include/FakeJNIEnv.h"

#define TAG "NativeIntegrationTest"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

// ----------------------
// Testes GTest
// ----------------------
class EqualizerNativeTest : public ::testing::Test {
protected:
    FakeJNIEnv env;
    jobject dummy = nullptr;

    void SetUp() override {
        Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_initialization(&env, dummy, 0);
    }

    std::string getStatus() {
        Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_getNativeStatus(&env, dummy);
        return env.captured_status;
    }
};

TEST_F(EqualizerNativeTest, InitialStateIsCorrectAfterSetup) {
    ASSERT_EQ(getStatus(), "enabled=false, volume=50");
    LOGI("Initial State test passed.");
}

TEST_F(EqualizerNativeTest, SetVolumeChangesStateCorrectly) {
    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(&env, dummy, 75);
    ASSERT_EQ(getStatus(), "enabled=false, volume=75");
    LOGI("Set Volume test passed.");
}

TEST_F(EqualizerNativeTest, EnableEqualizerChangesStateCorrectly) {
    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(&env, dummy, JNI_TRUE);
    ASSERT_EQ(getStatus(), "enabled=true, volume=50");
    LOGI("Enable Equalizer test passed.");
}

TEST_F(EqualizerNativeTest, CombinedStateChangeIsCorrect) {
    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setVolumeFromNative(&env, dummy, 100);
    Java_com_leandromendes_vehicleequalizer_modules_equalizer_EqualizerModule_setEqualizerEnabledNative(&env, dummy, JNI_TRUE);
    ASSERT_EQ(getStatus(), "enabled=true, volume=100");
    LOGI("Combined State test passed.");
}
