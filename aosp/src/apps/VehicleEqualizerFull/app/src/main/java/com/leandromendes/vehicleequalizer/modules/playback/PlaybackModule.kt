package com.leandromendes.vehicleequalizer.modules.playback

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.RawRes

class PlaybackModule(private val context: Context) : PlaybackInterface {

    private val logTAG = "PlaybackModule"
    private var mediaPlayer: MediaPlayer? = null
    private var onPreparedListener: (() -> Unit)? = null

    override fun setOnPreparedListener(listener: () -> Unit) {
        onPreparedListener = listener
    }

    override fun setRawDataSource(@RawRes resId: Int) {
        release()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            setOnPreparedListener {
                Log.d(logTAG, "MediaPlayer preparado")
                onPreparedListener?.invoke()
            }
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
        Log.d("PlaybackModule", "Avançando para posição: $position ms")
    }

    override fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    override fun getCurrentPosition(): Int {
        val position = mediaPlayer?.currentPosition ?: 0
        Log.d("PlaybackModule", "Posição atual: $position ms")
        return position
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("PlaybackModule", "MediaPlayer liberado")
    }

    override fun getAudioSessionId(): Int {
        val id = mediaPlayer?.audioSessionId ?: -1
        Log.d("PlaybackModule", "AudioSessionId solicitado: $id")
        return id
    }
}
