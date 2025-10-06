package com.leandromendes.vehicleequalizer.modules.playback

/**
 * Playback interface
 * Interface with all methods of the playbackModule class
 *
 * @constructor Create empty Playback interface
 */
interface PlaybackInterface {
    /**
     * Set on prepared listener
     * Register a listener that will be called when the media player is ready
     *
     * @param listener Callback
     */
    fun setOnPreparedListener(listener: () -> Unit)

    /**
     * Set raw data source
     *
     * @param resId Audio resource ID
     */
    fun setRawDataSource(resId: Int)

    /**
     * Play
     * Start playing the audio track
     */
    fun play()

    /**
     * Pause
     * Pause audio playback
     */
    fun pause()

    /**
     * Stop
     * Stop playing the audio track
     */
    fun stop()

    /**
     * Seek to
     * Changes the position of the audio track based on the position of the SeekBar
     *
     * @param position SeekBar position
     */
    fun seekTo(position: Int)

    /**
     * Get duration
     *
     * @return Get the duration of the loaded audio track
     */
    fun getDuration(): Int

    /**
     * Get current position
     *
     * @return Get the current position of the loaded audio track
     */
    fun getCurrentPosition(): Int

    /**
     * Is playing
     *
     * @return Returns the status of the music player
     */
    fun isPlaying(): Boolean

    /**
     * Get audio session id
     *
     * @return Returns the ID of the audio section
     */
    fun getAudioSessionId(): Int

    /**
     * Release
     * Release player resource
     */
    fun release()
}
