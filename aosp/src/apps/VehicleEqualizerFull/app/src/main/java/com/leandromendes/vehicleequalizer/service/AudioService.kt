package com.leandromendes.vehicleequalizer.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerInterface
import com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerModule
import com.leandromendes.vehicleequalizer.modules.notification.NotificationInterface
import com.leandromendes.vehicleequalizer.modules.notification.NotificationModule
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackInterface
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import com.leandromendes.vehicleequalizer.util.Constants.PlaybackStates

class AudioService : Service() {

    private val logTAG ="AudioService"
    private lateinit var playbackModule: PlaybackInterface
    private lateinit var notificationModule: NotificationInterface
    private var equalizerModule: EqualizerInterface? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable : Runnable
    private var currentTrackIndex = 0
    private var isStopped = true
    private lateinit var trackList : List<Track>

    override fun onCreate() {
        super.onCreate()
        notificationModule = NotificationModule(this)
        playbackModule = PlaybackModule(this)

        // 🔑 só inicializa equalizer quando MediaPlayer estiver pronto
        if (playbackModule is PlaybackModule) {
            (playbackModule as PlaybackModule).setOnPreparedListener {
                initEqualizerIfNeeded()
            }
        }

        trackList = listOf( // List of songs for testing
            Track(R.raw.toto_africa, "Toto Africa"),
            Track(R.raw.spain_yohan_kim_friends_concert_live, "Yohan Kim Friends")
        )

        loadTrack(currentTrackIndex)

        //  Start notification already in stopped state
        startForeground(
            NotificationModule.NOTIFICATION_ID,
            notificationModule.buildNotification(
                PlaybackStates.STOPPED,
                trackList[currentTrackIndex].title
            )
        )

        // Atualizações periódicas (posição do player)
        updateRunnable = object : Runnable {
            override fun run() {
                broadcastState()
                if (playbackModule.isPlaying()) {
                    handler.removeCallbacks(this) // Avoids duplication
                    handler.postDelayed(this, 1000)
                }
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            MusicConstants.ACTION_PLAY -> {
                if (isStopped) loadTrack(currentTrackIndex)

                playbackModule.play()
                handler.post(updateRunnable)
                notificationModule.showNotification(
                    PlaybackStates.PLAYING,
                    trackList[currentTrackIndex].title
                )

                isStopped = false
            }

            MusicConstants.ACTION_PAUSE -> {
                playbackModule.pause()
                notificationModule.showNotification(
                    PlaybackStates.PAUSED,
                    trackList[currentTrackIndex].title
                )

                isStopped = false
            }

            MusicConstants.ACTION_STOP -> {
                playbackModule.stop()
                notificationModule.showNotification(
                    PlaybackStates.STOPPED,
                    trackList[currentTrackIndex].title
                )

                isStopped = true
            }

            MusicConstants.ACTION_NEXT -> {
                currentTrackIndex = (currentTrackIndex + 1) % trackList.size
                loadTrack(currentTrackIndex)
                playbackModule.play()
                handler.post(updateRunnable)
                notificationModule.showNotification(
                    PlaybackStates.PLAYING,
                    trackList[currentTrackIndex].title
                )

                isStopped = false
            }

            MusicConstants.ACTION_PREVIOUS -> {
                currentTrackIndex =
                    if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
                loadTrack(currentTrackIndex)
                playbackModule.play()
                handler.post(updateRunnable)
                notificationModule.showNotification(
                    PlaybackStates.PLAYING,
                    trackList[currentTrackIndex].title
                )

                isStopped = false
            }

            MusicConstants.ACTION_SEEK_TO -> {
                val pos = intent.getIntExtra(MusicConstants.EXTRA_SEEK_POSITION, 0)
                playbackModule.seekTo(pos)
            }

            MusicConstants.ACTION_APPLY_PROFILE -> {
                val profile =
                    intent.getParcelableExtra(
                        MusicConstants.EXTRA_PROFILE,
                        EqualizerProfile::class.java
                    )
                profile?.let { equalizerModule?.applyProfile(it) }
            }

            MusicConstants.ACTION_ENABLE_EQUALIZER -> {
                val enabled = intent.getBooleanExtra(MusicConstants.EXTRA_ENABLED, true)
                equalizerModule?.enable(enabled)
            }

            MusicConstants.ACTION_SET_BASS -> {
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 0)
                equalizerModule?.setBass(level)
            }

            MusicConstants.ACTION_SET_MID -> {
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 0)
                equalizerModule?.setMid(level)
            }

            MusicConstants.ACTION_SET_TREBLE -> {
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 0)
                equalizerModule?.setTreble(level)
            }

            MusicConstants.ACTION_SET_VOLUME -> {
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 5)
                equalizerModule?.setVolume(level)
            }

            MusicConstants.ACTION_SET_BALANCE -> {
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 0)
                equalizerModule?.setBalance(level)
            }

        }

        // Updates notification and starts as foreground
        startForeground(
            NotificationModule.NOTIFICATION_ID,
            notificationModule.buildNotification(
                playbackState(),
                trackList[currentTrackIndex].title
            )
        )

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackModule.release()
        equalizerModule?.release() // 🔑 libera o equalizer
        handler.removeCallbacks(updateRunnable)
        notificationModule.cancelNotification()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun loadTrack(index: Int) {
        val track = trackList[index]
        playbackModule.setRawDataSource(track.resId)
    }

    private fun broadcastState() {
        val intent = Intent(MusicConstants.BROADCAST_MUSIC_STATE).apply {
            putExtra(MusicConstants.EXTRA_STATE, playbackState())
            putExtra(MusicConstants.EXTRA_CURRENT_POSITION, playbackModule.getCurrentPosition())
            putExtra(MusicConstants.EXTRA_DURATION, playbackModule.getDuration())
            putExtra(MusicConstants.EXTRA_TRACK_TITLE, trackList[currentTrackIndex].title)
        }
        sendBroadcast(intent)
    }

    private fun playbackState(): String =
        if (isStopped) PlaybackStates.STOPPED
        else if (playbackModule.isPlaying()) PlaybackStates.PLAYING
        else PlaybackStates.PAUSED

    private fun initEqualizerIfNeeded() {
        val sessionId = playbackModule.getAudioSessionId()
        if (sessionId > 0) {
            try {
                equalizerModule = EqualizerModule(this, sessionId)
                Log.d(logTAG, "Equalizer inicializado com sessionId=$sessionId")
                equalizerModule?.printBandsInfo()
            } catch (e: Exception) {
                Log.e(logTAG, "Falha ao inicializar Equalizer: ${e.message}")
            }
        }
    }

    data class Track(val resId: Int, val title: String)
}
