package com.leandromendes.vehicleequalizer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.leandromendes.vehicleequalizer.modules.notification.NotificationModule
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
class AudioService : Service() {

    private lateinit var playbackModule: PlaybackModule
    private lateinit var notificationModule: NotificationModule

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
        playbackModule = PlaybackModule(this)
        notificationModule = NotificationModule(this)
        notificationModule.showNotification("paused")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "com.leandromendes.vehicleequalizer.PLAY" -> {
                playbackModule.play()
                notificationModule.updateNotification("playing")
            }
            "com.leandromendes.vehicleequalizer.PAUSE" -> {
                playbackModule.pause()
                notificationModule.updateNotification("paused")
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackModule.release()
        notificationModule.cancelNotification()
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "music_player_channel"
            val channelName = "Controles de Música"
            val channelDescription = "Notificações para controle de reprodução"
            val importance = NotificationManager.IMPORTANCE_LOW

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                setShowBadge(false)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
