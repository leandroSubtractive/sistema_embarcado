package com.leandromendes.vehicleequalizer.modules.equalizer

import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

interface EqualizerInterface {
    fun applyProfile(profile: EqualizerProfile)
    fun setBass(level: Int)
    fun setMid(level: Int)
    fun setTreble(level: Int)
    fun setBalance(level: Int) // opcional
    fun setVolume(level: Int)
    fun setBandLevelSafe(band: Int, level: Int)
    fun enable(enabled: Boolean)
    fun reset()
    fun printBandsInfo()
    fun release()
}