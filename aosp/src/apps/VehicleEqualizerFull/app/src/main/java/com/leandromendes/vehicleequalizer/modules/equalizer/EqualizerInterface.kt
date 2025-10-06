package com.leandromendes.vehicleequalizer.modules.equalizer

import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

interface EqualizerInterface {

    /**
     * Apply profile
     *
     * @param profile Audio Profile
     */
    fun applyProfile(profile: EqualizerProfile)

    /**
     * Set volume
     *
     * @param level Volume value in percent [0 - 100]
     */
    fun setVolume(level: Int)

    /**
     * Set band level safe
     *
     * @param band Band ID [0 - 4]
     * @param level Gain in dB [-15 - 15]
     */
    fun setBandLevelSafe(band: Int, level: Int)

    /**
     * Set enable
     *
     * @param enabled Equalizer Status
     */
    fun setEnable(enabled: Boolean)

    /**
     * Get enabled
     *
     * @return Current status
     */
    fun getEnabled(): Boolean

    /**
     * Reset
     * Reset to default values 0db
     */
    fun reset()

    /**
     * Release
     * Enable equalizer feature
     */
    fun release()

    /**
     * Print bands info
     * Displays all equalizer information on the terminal, used for Log
     */
    fun printBandsInfo()
}