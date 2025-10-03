package com.leandromendes.vehicleequalizer.modules.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log

class PlaybackModule(private val context: Context) : PlaybackInterface {

    private var mediaPlayer: MediaPlayer? = null
    private var currentRawResId: Int? = null

    private val TAG = "PlaybackModule"

    override fun play() {
        try {
            if (mediaPlayer == null) {
                Log.e(TAG, "Nenhuma faixa foi carregada. Use setRawDataSource(resId) primeiro.")
                return
            }

            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer?.start()
                Log.d(TAG, "Reprodução iniciada.")
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Erro ao iniciar reprodução: ${e.message}")
        }
    }

    override fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                Log.d(TAG, "Reprodução pausada.")
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Erro ao pausar: ${e.message}")
        }
    }

    override fun stop() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                Log.d(TAG, "Reprodução parada e MediaPlayer liberado.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao parar: ${e.message}")
        }
    }

    override fun seekTo(position: Int) {
        try {
            mediaPlayer?.seekTo(position)
            Log.d(TAG, "Buscando para posição: $position")
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Erro no seek: ${e.message}")
        }
    }

    fun setRawDataSource(resId: Int) {
        try {
            if (resId == currentRawResId) {
                Log.d(TAG, "A mesma faixa já está carregada.")
                return
            }

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, resId)

            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            mediaPlayer?.setOnCompletionListener {
                Log.d(TAG, "Reprodução concluída.")
            }

            currentRawResId = resId
            Log.d(TAG, "Recurso carregado do raw: $resId")

        } catch (e: Exception) {
            Log.e(TAG, "Erro ao configurar data source: ${e.message}")
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d(TAG, "MediaPlayer liberado.")
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun canPlay(): Boolean {
        return mediaPlayer != null
    }
}
