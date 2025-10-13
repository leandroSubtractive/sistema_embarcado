package com.leandromendes.vehicleequalizer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import org.junit.Assert
import org.junit.Before
import org.junit.After
import org.junit.Test

/**
 * Unit tests for the audio playback module
 *
 * Validates the control methods: play, pause, and stop
 */
class PlaybackModuleTest {

    private lateinit var context: Context
    private lateinit var playbackModule: PlaybackModule

    @Before
    fun setup() {
        // Gets the application context and initializes the playback module
        context = ApplicationProvider.getApplicationContext()
        playbackModule = PlaybackModule(context)
    }

    @After
    fun tearDown() {
        playbackModule.stop()
    }

    // Test audio playback
    @Test
    fun testPlayAudio() {
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()
        Assert.assertTrue("The audio should be playing after play()", playbackModule.isPlaying())
        playbackModule.stop()
    }

    // Test the audio pause
    @Test
    fun testPauseAudio() {
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()
        playbackModule.pause()
        Assert.assertFalse("The audio should be paused after pause().", playbackModule.isPlaying())
    }

    // Tests playback pause
    @Test
    fun testStopAudio() {
        playbackModule.setRawDataSource(R.raw.toto_africa)
        playbackModule.play()
        playbackModule.stop()
        Assert.assertFalse("The audio should be paused after stop()", playbackModule.isPlaying())
    }
}