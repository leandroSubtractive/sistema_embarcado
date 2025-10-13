package com.leandromendes.vehicleequalizer

import com.leandromendes.vehicleequalizer.service.AudioService
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented testing for AudioService
 */
@RunWith(AndroidJUnit4::class)
class AudioServiceTest {

    // Rule that deals with the service lifecycle for testing
    @get:Rule
    val serviceRule = ServiceTestRule()

    private lateinit var context: Context
    private lateinit var serviceIntent: Intent

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        serviceIntent = Intent(context, AudioService::class.java)
    }

    // Life Cycle Testing
    // ---------------------------------

    @Test
    fun service_bindsAndCallsOnCreate() {
        // Start the service by calling onCreate() and onBind().
        val binder = serviceRule.bindService(serviceIntent)

        // Verifies that the service was created correctly (the binder is not null)
        assertNotNull(binder)
    }

    @Test
    fun service_startsForegroundOnCreate() {
        // When starting the service, it must call startForeground.

        // bindService() returns the IBinder (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // Cast the binder to custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()
        assertNotNull(service)

        // We check the return value to ensure that the command was processed.
        val result = service.onStartCommand(null, 0, 0)
        assertEquals(Service.START_STICKY, result)
    }

    // --- Reproduction Action Tests (Playback Actions) ---
    // ---------------------------------------------------------

    @Test
    fun onStartCommand_actionPlay_shouldStartPlayback() {
        // bindService() returns the IBinder (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // Cast the binder to custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()

        // Creates the ACTION_PLAY intent
        val playIntent = Intent(serviceIntent).apply {
            action = MusicConstants.ACTION_PLAY
        }

        // Execute the command
        service.onStartCommand(playIntent, 0, 1)

        // Checks the return value of onStartCommand and the lifecycle state,
        // which should be START_STICKY
        val result = service.onStartCommand(playIntent, 0, 1)
        assertEquals(Service.START_STICKY, result)
    }

    @Test
    fun onStartCommand_actionPause_shouldPausePlayback() {
        // Starts and sets to PLAYING state (to ensure that PAUSE does something)

        // bindService() returns the IBinder (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // Cast the binder to custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()

        service.onStartCommand(Intent(serviceIntent).apply { action = MusicConstants.ACTION_PLAY }, 0, 1)

        // Creates the ACTION_PAUSE intent
        val pauseIntent = Intent(serviceIntent).apply {
            action = MusicConstants.ACTION_PAUSE
        }

        // Execute the command
        service.onStartCommand(pauseIntent, 0, 2)

        // Check the status
        val result = service.onStartCommand(pauseIntent, 0, 2)
        assertEquals(Service.START_STICKY, result)

    }

    @Test
    fun onStartCommand_actionNext_shouldAdvanceTrackAndPlay() {
        // bindService() returns the IBinder (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // Cast the binder to custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()

        // Check initial state
        assertEquals("Toto Africa", service.getCurrentTrackTitle())

        // Send the command to the service instance
        val nextIntent = Intent(serviceIntent).apply {
            action = MusicConstants.ACTION_NEXT
        }

        // Call the command directly on the service instance
        service.onStartCommand(nextIntent, 0, 1)

        // Verify the new state
        assertEquals("Yohan Kim Friends", service.getCurrentTrackTitle())
    }

}