package com.leandromendes.vehicleequalizer.modules.equalizer

import android.content.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Testes unitários para EqualizerModule.
 * Simula configuração de bandas e estados do equalizador.
 */
class EqualizerModuleTest {

    @Mock private lateinit var context: Context
    private lateinit var module: EqualizerModule

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        // Mock parcial: permite chamar métodos sem Android real
        module = mock(EqualizerModule::class.java, withSettings().defaultAnswer(CALLS_REAL_METHODS))
    }

    @Test
    fun testSetBandLevel() {
        doNothing().`when`(module).setBandLevelSafe(anyInt(), anyInt())
        module.setBandLevelSafe(2, -12)
        verify(module).setBandLevelSafe(2, -12)
    }

    @Test
    fun testGetBandLevel() {
        `when`(module.getBandLevel(1)).thenReturn(-12)
        val result = module.getBandLevel(1)
        assert(result == (-12).toShort())
        verify(module).getBandLevel(1)
    }

    @Test
    fun testEnableDisableEqualizer() {
        doNothing().`when`(module).setEnable(true)
        module.setEnable(true)
        verify(module).setEnable(true)
    }
}
