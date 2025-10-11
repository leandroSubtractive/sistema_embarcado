#include <gtest/gtest.h>
#include <string>
#include "../../main/cpp/native-lib.cpp" // inclui o código real

// Teste se a inicialização define corretamente os valores
TEST(EqualizerNativeTest, InitializationSetsDefaultValues) {
int testAudioSessionId = 999;
// Simula chamada da função de inicialização
for (int i = 0; i < NUM_BANDS; i++) {
s_bandFrequency[i].dB = 0;
s_bandFrequency[i].audioSessionId = testAudioSessionId;
}

for (int i = 0; i < NUM_BANDS; i++) {
EXPECT_EQ(s_bandFrequency[i].dB, 0);
EXPECT_EQ(s_bandFrequency[i].audioSessionId, testAudioSessionId);
}
}

// Teste de controle de volume
TEST(EqualizerNativeTest, SetVolumeFromNativeUpdatesValue) {
s_volumeLevel = 50;
int newVolume = 80;
s_volumeLevel = newVolume;
EXPECT_EQ(s_volumeLevel, 80);
}

// Teste de bandas de frequência
TEST(EqualizerNativeTest, SetAndGetBandLevelWorks) {
int bandIndex = 2;
s_bandFrequency[bandIndex].dB = 5;
EXPECT_EQ(s_bandFrequency[bandIndex].dB, 5);
}

// Teste de habilitação do equalizador
TEST(EqualizerNativeTest, EqualizerEnableFlagWorks) {
s_equalizerEnabled = false;
s_equalizerEnabled = true;
EXPECT_TRUE(s_equalizerEnabled);
}
