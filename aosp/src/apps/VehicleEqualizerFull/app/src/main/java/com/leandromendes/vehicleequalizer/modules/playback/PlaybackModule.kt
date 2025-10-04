package com.leandromendes.vehicleequalizer.modules.playback

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.RawRes

class PlaybackModule(private val context: Context) : PlaybackInterface {

    private val logTAG = "PlaybackModule"
    private var mediaPlayer: MediaPlayer? = null

    override fun setRawDataSource(@RawRes resId: Int) {
        release()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            setOnCompletionListener {
                Log.d(logTAG, "Finished track.")
            }
        }
    }

    override fun play() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                Log.d(logTAG, "Playback started.")
            }
        }
    }

    override fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                Log.d(logTAG, "Paused playback.")
            }
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                Log.d(logTAG, "Stop playback.")
            }
        }
        release()
    }

    override fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun getDuration(): Int = mediaPlayer?.duration ?: 0

    override fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
