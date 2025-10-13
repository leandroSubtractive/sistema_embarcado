package com.leandromendes.vehicleequalizer

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Native integration test
 *
 * Check basic functionality of native functions
 */
@RunWith(AndroidJUnit4::class)
class NativeIntegrationTest {

    private lateinit var equalizerModule: EqualizerModule

    @Before
    fun setup() {
        // Initializes the module before each test.
        equalizerModule = EqualizerModule(ApplicationProvider.getApplicationContext(), 100)
        // Calls native initialization with a sample audio session ID.
        equalizerModule.setEqualizerEnabledNative(false)
        equalizerModule.setVolumeFromNative(50)
    }

    @Test
    fun testInitializationAndStatus() {
        val status = equalizerModule.getNativeStatus()
        // Checks whether initialization has occurred (default volume 50 and disabled).
        Assert.assertEquals("enabled=false, volume=50", status)
    }

    @Test
    fun testSetVolume() {
        val expectedVolume = 75
        equalizerModule.setVolumeFromNative(expectedVolume)
        // Verify that the volume has been set to the specified value.
        val status = equalizerModule.getNativeStatus()
        Assert.assertEquals("enabled=false, volume=$expectedVolume", status)

    }

    @Test
    fun testSetBandLevel() {
        val bandId = 1 // 910Hz
        val level = 12 // +12dB

        equalizerModule.setBandLevelNative(bandId, level)
        val retrievedLevel = equalizerModule.getBandLevelNative(bandId)
        // Check that the frequency band has been set to the correct value
        Assert.assertEquals(level, retrievedLevel)
    }

    @Test
    fun testEnableEqualizer() {
        equalizerModule.setEqualizerEnabledNative(true)
        val status = equalizerModule.getNativeStatus()
        // Check if the equalizer has been enabled
        Assert.assertTrue(status.contains("enabled=true"))
    }
}