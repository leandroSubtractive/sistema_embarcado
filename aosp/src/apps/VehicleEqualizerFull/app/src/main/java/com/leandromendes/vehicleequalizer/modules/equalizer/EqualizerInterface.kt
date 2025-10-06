package com.leandromendes.vehicleequalizer.modules.equalizer

import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

interface EqualizerInterface {
    fun applyProfile(profile: EqualizerProfile)
    fun setVolume(level: Int)
    fun setBandLevelSafe(band: Int, level: Int)
    fun setEnable(enabled: Boolean)
    fun getEnabled(): Boolean
    fun reset()
    fun release()
    fun printBandsInfo()
}