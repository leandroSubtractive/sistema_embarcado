package com.leandromendes.vehicleequalizer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Testes unitários para o módulo de reprodução de áudio.
 * Valida os métodos de controle: play, pause e stop.
 */
class PlaybackModuleTest {

    private lateinit var context: Context
    private lateinit var playbackModule: PlaybackModule

    @Before
    fun setup() {
        // Obtém o contexto da aplicação e inicializa o módulo de reprodução
        context = ApplicationProvider.getApplicationContext()
        playbackModule = PlaybackModule(context)
    }

    /**
     * Testa a execução de áudio.
     */
    @Test
    fun testPlayAudio() {
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()
        Assert.assertTrue("O áudio deveria estar tocando após play()", playbackModule.isPlaying())
    }

    /**
     * Testa a pausa do áudio.
     */
    @Test
    fun testPauseAudio() {
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()
        playbackModule.pause()
        Assert.assertFalse("O áudio deveria estar pausado após pause()", playbackModule.isPlaying())
    }

    /**
     * Testa a parada da reprodução.
     */
    @Test
    fun testStopAudio() {
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()
        playbackModule.stop()
        Assert.assertFalse("O áudio deveria estar parado após stop()", playbackModule.isPlaying())
    }
}