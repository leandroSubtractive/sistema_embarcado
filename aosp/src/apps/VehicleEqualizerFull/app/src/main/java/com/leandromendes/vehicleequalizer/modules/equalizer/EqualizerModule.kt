package com.leandromendes.vehicleequalizer.modules.equalizer

import android.content.Context
import android.media.AudioManager
import android.media.audiofx.Equalizer
import android.util.Log
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile


class EqualizerModule(
    private val context: Context,
    audioSessionId: Int
) : EqualizerInterface {

    private var logTAG = "EqualizerModule"
    private var enabled = true


    private var equalizer: Equalizer? = try {
        if (audioSessionId > 0) {
            Equalizer(0, audioSessionId).apply { enabled = true }
        } else {
            Log.e(logTAG, "Invalid audioSessionId ($audioSessionId)")
            null
        }
    } catch (e: Exception) {
        Log.e(logTAG, "Failed to initialize Equalizer: ${e.message}")
        null
    }

    override fun applyProfile(profile: EqualizerProfile) {
        if (!enabled) {
            Log.d(logTAG, "Equalizer disabled does not apply profile")
            return
        }

        Log.d(logTAG, "Applying profile: $profile")
        setBandLevelSafe(0,profile.band0)
        setBandLevelSafe(1,profile.band1)
        setBandLevelSafe(2,profile.band2)
        setBandLevelSafe(3,profile.band3)
        setBandLevelSafe(4,profile.band4)
        setVolume(profile.masterVolValue)
    }

    override fun setVolume(level: Int) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val newVolume = ((level.toFloat() / 100f) * maxVolume).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
            setVolumeFromNative(newVolume)
            Log.d(logTAG, "Volume adjusted to $level (stream=$newVolume/$maxVolume)")
        } catch (e: Exception) {
            Log.e(logTAG, "Error adjusting volume: ${e.message}")
        }
    }

    override fun setBandLevelSafe(band: Int, level: Int) {
        val eq = equalizer ?: return
        if (!enabled) return

        try {
            val range = eq.bandLevelRange // [min, max] in tenths of dB
            val min = range[0]
            val max = range[1]

            // ensures that the value is within the supported range
            val safeLevel = level.coerceIn(min.toInt(), max.toInt()).toShort()

            eq.setBandLevel(band.toShort(), ((safeLevel * 100).toShort()))
            setBandLevelNative(band, safeLevel.toInt())
            Log.d(logTAG, "Band $band Adjusted for ${(safeLevel * 100)} (range: $min .. $max)")
        } catch (e: Exception) {
            Log.e(logTAG, "Error when adjusting band $band: ${e.message}")
        }
    }

    override fun setEnable(enabled: Boolean) {
        this.enabled = enabled
        equalizer?.enabled = enabled
        setEqualizerEnabledNative(enabled)
        Log.d(logTAG, "Equalizer ${if (enabled) "Enabled" else "Disabled"}")
    }

    override fun getEnabled(): Boolean {
        return equalizer?.enabled == true
    }

    override fun reset() {
        if (!enabled) {
            Log.d(logTAG, "Disabled equalizer does not execute reset() command")
            return
        }
        for (i in 0 until equalizer!!.numberOfBands) {
            equalizer!!.setBandLevel(i.toShort(), 0)
        }
        Log.d(logTAG, "Equalizer reset (all bands = 0dB)")
    }

    override fun release() {
        equalizer?.release()
        equalizer = null
        Log.d(logTAG, "Equalizer released")
    }

    override fun printBandsInfo() {
        val bands = equalizer?.numberOfBands ?: 0
        val range = equalizer?.bandLevelRange ?: shortArrayOf(0, 0)

        Log.d(logTAG, "Equalizer has $bands bands (Min=${range[0]} dB, Max=${range[1]} dB)")

        for (i in 0 until bands) {
            val centerFreq = equalizer?.getCenterFreq(i.toShort()) ?: 0
            Log.d(logTAG, "Band $i → Freq: ${centerFreq / 1000} Hz")
        }
    }
    /**
     * A native method that is implemented by the 'vehicleequalizer' native library,
     * which is packaged with this application.
     */
    external fun setEqualizerEnabledNative( enabled: Boolean)
    external fun setBandLevelNative(band: Int, level: Int)
    external fun setVolumeFromNative(volume: Int)


    companion object {
        // Used to load the 'vehicleequalizer' library on application startup.
        init {
            System.loadLibrary("vehicleequalizer")
        }
    }
}
