package com.leandromendes.vehicleequalizer.modules.playback

interface PlaybackInterface {
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Int)
}