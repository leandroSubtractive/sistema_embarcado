package com.leandromendes.vehicleequalizer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerModule
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Instrumented tests of the EqualizerModule module
 *
 * Checks initialization and control of frequency bands
 */
class EqualizerModuleTest {

    private lateinit var context: Context
    private lateinit var equalizerModule: EqualizerModule

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        // Initializes MediaPlayer and obtains the actual audioSessionId
        val playbackModule = PlaybackModule(context)
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()

        val sessionId = playbackModule.getAudioSessionId()

        // Initializes the equalizer with the active audio session
        equalizerModule = EqualizerModule(context, sessionId)
    }

    @Test
    fun testSetBandLevel() {
        val band = 0
        val level = -15
        equalizerModule.setBandLevelSafe(band, level)
        assertEquals("Incorrect band level", level.toShort(), equalizerModule.getBandLevel(band))
    }
}
