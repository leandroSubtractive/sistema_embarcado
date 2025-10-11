package com.leandromendes.vehicleequalizer.modules.equalizer // Adapte ao seu pacote

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeIntegrationTest {

    private lateinit var equalizerModule: EqualizerModule

    @Before
    fun setup() {
        // Inicializa o módulo antes de cada teste.
        equalizerModule = EqualizerModule(ApplicationProvider.getApplicationContext(), 100)
        // Chama a inicialização nativa com um ID de sessão de áudio de exemplo.
        equalizerModule.setEqualizerEnabledNative(false)
        equalizerModule.setVolumeFromNative(50)
    }

    @Test
    fun testInitializationAndStatus() {
        val status = equalizerModule.getNativeStatus()
        // Verifica se a inicialização ocorreu (volume padrão 50 e desativado).
        assertEquals("enabled=false, volume=50", status)
    }

    @Test
    fun testSetVolume() {
        val expectedVolume = 75
        equalizerModule.setVolumeFromNative(expectedVolume)

        // Usa a função de consulta de estado nativa para verificar o volume
        val status = equalizerModule.getNativeStatus()
        assertEquals("enabled=false, volume=$expectedVolume", status)

        // Dica: Verifique o Logcat para ver as mensagens LOGI do C++: "Volume: 75"
    }

    @Test
    fun testSetBandLevel() {
        val bandId = 1 // 910Hz
        val level = 12 // +12dB

        equalizerModule.setBandLevelNative(1, level)
        val retrievedLevel = equalizerModule.getBandLevelNative(1)

        assertEquals(level, retrievedLevel)

        // Dica: Verifique o Logcat para a mensagem LOGI do C++: "Band 910Hz gain 12dB"
    }

    @Test
    fun testEnableEqualizer() {
        equalizerModule.setEqualizerEnabledNative(true)
        val status = equalizerModule.getNativeStatus()

        assertTrue(status.contains("enabled=true"))
        // Dica: Verifique o Logcat para a mensagem LOGI do C++: "Native equalizer activated"
    }
}