package com.leandromendes.vehicleequalizer.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Binder
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
    private var lastProfile: EqualizerProfile? = null
    private var isStopped = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable : Runnable
    private var currentTrackIndex = 0
    private lateinit var trackList : List<Track>
    // Helper class for the non-binding binder
    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        Log.d(logTAG, "Service created")

        notificationModule = NotificationModule(this)
        playbackModule = PlaybackModule(this)

        // Only initialize the equalizer when MediaPlayer is ready
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

        // Periodic updates (player position)
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
                ensureEqualizerInitialized()
                lastProfile =
                    intent.getParcelableExtra(
                        MusicConstants.EXTRA_PROFILE,
                        EqualizerProfile::class.java
                    )
                applyLastProfile(lastProfile)
            }

            MusicConstants.ACTION_ENABLE_EQUALIZER -> {
                ensureEqualizerInitialized()
                val enabled = intent.getBooleanExtra(MusicConstants.EXTRA_ENABLED, true)
                equalizerModule?.setEnable(enabled)

                // If enabled, reapply the profile
                if(enabled){
                    applyLastProfile(lastProfile)
                }
            }

            MusicConstants.ACTION_SET_BAND_LEVEL -> {
                ensureEqualizerInitialized()
                val band = intent.getIntExtra(MusicConstants.EXTRA_BAND, 0)
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 0)
                equalizerModule?.setBandLevelSafe(band, level)
            }

            MusicConstants.ACTION_SET_VOLUME -> {
                ensureEqualizerInitialized()
                val level = intent.getIntExtra(MusicConstants.EXTRA_LEVEL, 5)
                equalizerModule?.setVolume(level)
            }

            MusicConstants.ACTION_EQUALIZER_STATUS -> {
                val intent = Intent(MusicConstants.ACTION_UPDATE_UI).apply {
                    // Equalizer status
                    putExtra(MusicConstants.EXTRA_EQUALIZER_ENABLED, equalizerModule?.getEnabled() ?: false)
                }
                sendBroadcast(intent)
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
        equalizerModule?.release()
        equalizerModule = null
        handler.removeCallbacks(updateRunnable)
        notificationModule.cancelNotification()
    }

    /**
     * Load track
     *
     * @param index Track index
     */
    private fun loadTrack(index: Int) {
        val track = trackList[index]
        playbackModule.setRawDataSource(track.resId)
    }

    /**
     * Apply last profile
     *
     * @param profile Equalization profile
     */
    private fun applyLastProfile(profile: EqualizerProfile?) {
        profile?.let { equalizerModule?.applyProfile(it) }
    }

    /**
     * Broadcast state
     * states that the service sends
     */
    private fun broadcastState() {
        val intent = Intent(MusicConstants.BROADCAST_MUSIC_STATE).apply {
            // State of music playback
            putExtra(MusicConstants.EXTRA_STATE, playbackState())
            // Get the current position of the song to update the counter in the interface
            putExtra(MusicConstants.EXTRA_CURRENT_POSITION, playbackModule.getCurrentPosition())
            // obtains the total duration of the song
            putExtra(MusicConstants.EXTRA_DURATION, playbackModule.getDuration())
            // Current song title
            putExtra(MusicConstants.EXTRA_TRACK_TITLE, trackList[currentTrackIndex].title)
        }
        sendBroadcast(intent)
    }

    /**
     * Playback state
     * Get the equalizer status
     *
     * @return Equalizer status
     */
    private fun playbackState(): String =
        if (isStopped) PlaybackStates.STOPPED
        else if (playbackModule.isPlaying()) PlaybackStates.PLAYING
        else PlaybackStates.PAUSED

    /**
     * Ensure equalizer initialized
     *
     */
    private fun ensureEqualizerInitialized() {
        if (equalizerModule == null) {
            initEqualizerIfNeeded()
        }
    }

    /**
     * Init equalizer if needed
     *
     */
    private fun initEqualizerIfNeeded() {
        val sessionId = playbackModule.getAudioSessionId()
        if (sessionId > 0) {
            try {
                equalizerModule = EqualizerModule(this, sessionId)
                applyLastProfile(lastProfile)
                Log.d(logTAG, "Equalizer initialized with sessionId=$sessionId")
                equalizerModule?.printBandsInfo()
            } catch (e: Exception) {
                Log.e(logTAG, "Failed to initialize Equalizer: ${e.message}")
            }
        }
    }

    /**
     * Helper class for binding the service.
     * Note: 'inner' allows it to access outer class members (like 'this@AudioService')
     */
    inner class LocalBinder : Binder() {
        // Method to return the service instance itself
        fun getService(): AudioService = this@AudioService
    }

    /**
     * On bind
     *
     */
    override fun onBind(intent: Intent?): IBinder? = binder

    /**
     * Track
     *
     * @property resId Resource ID
     * @property title Song name
     * @constructor Create empty Track
     */
    data class Track(val resId: Int, val title: String)

    // Add this public function inside the AudioService class
    fun getCurrentTrackTitle(): String {
        // Note: You might need to make 'currentTrackIndex' internal/public
        // or pass it to this function if you want to verify specific tracks.
        if (::trackList.isInitialized) {
            return trackList[currentTrackIndex].title
        }
        return "Unknown"
    }

}
