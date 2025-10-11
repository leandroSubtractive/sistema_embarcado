package com.leandromendes.vehicleequalizer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerModule
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Testes instrumentados do módulo EqualizerModule.
 * Verifica inicialização e controle das bandas de frequência.
 */
class EqualizerModuleTest {

    private lateinit var context: Context
    private lateinit var equalizerModule: EqualizerModule

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        // Inicializa o MediaPlayer e obtém o audioSessionId real
        val playbackModule = PlaybackModule(context)
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()

        val sessionId = playbackModule.getAudioSessionId()

        // Inicializa o equalizador com a sessão de áudio ativa
        equalizerModule = EqualizerModule(context, sessionId)
    }

    @Test
    fun testSetBandLevel() {
        val band = 0
        val level = -15
        equalizerModule.setBandLevelSafe(band, level)
        assertEquals("Nível da banda incorreto", level.toShort(), equalizerModule.getBandLevel(band))
    }
}
