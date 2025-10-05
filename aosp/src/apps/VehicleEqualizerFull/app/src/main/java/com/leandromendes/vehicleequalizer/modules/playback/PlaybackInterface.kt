package com.leandromendes.vehicleequalizer.modules.playback

interface PlaybackInterface {
    fun setOnPreparedListener(listener: () -> Unit)
    fun setRawDataSource(resId: Int)
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Int)
    fun getDuration(): Int
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun release()
    fun getAudioSessionId(): Int
}
