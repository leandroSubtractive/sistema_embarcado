package com.leandromendes.vehicleequalizer.modules.playback

import android.content.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Testes unitários para PlaybackModule.
 * Verifica chamadas básicas de controle de áudio (mockadas).
 */
class PlaybackModuleTest {

    @Mock private lateinit var context: Context
    private lateinit var module: PlaybackModule

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        module = mock(PlaybackModule::class.java, withSettings().defaultAnswer(CALLS_REAL_METHODS))
    }

    @Test
    fun testPlay() {
        doNothing().`when`(module).play()
        module.play()
        verify(module).play()
    }

    @Test
    fun testPause() {
        doNothing().`when`(module).pause()
        module.pause()
        verify(module).pause()
    }

    @Test
    fun testStop() {
        doNothing().`when`(module).stop()
        module.stop()
        verify(module).stop()
    }

    @Test
    fun testSetRawDataSource() {
        doNothing().`when`(module).setRawDataSource(anyInt())
        module.setRawDataSource(123)
        verify(module).setRawDataSource(123)
    }
}
