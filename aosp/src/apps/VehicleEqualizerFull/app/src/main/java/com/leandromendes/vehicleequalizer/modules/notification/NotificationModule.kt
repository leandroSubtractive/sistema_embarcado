package com.leandromendes.vehicleequalizer.modules.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.leandromendes.vehicleequalizer.MainActivity
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.service.AudioService

class NotificationModule(private val context: Context) : NotificationInterface {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "music_player_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun showNotification(playbackState: String) {
        val notification = buildNotification(playbackState)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun updateNotification(playbackState: String) {
        val notification = buildNotification(playbackState)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun buildNotification(playbackState: String): Notification {
        val playPauseAction = if (playbackState == "playing") {
            NotificationCompat.Action(
                R.drawable.ic_pause,
                "Pause",
                getPausePendingIntent()
            )
        } else {
            NotificationCompat.Action(
                R.drawable.ic_play,
                "Play",
                getPlayPendingIntent()
            )
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Reprodução de Música")
            .setContentText("Estado: $playbackState")
            .setSmallIcon(R.drawable.ic_music_note)
            .addAction(playPauseAction)
            .setOngoing(true)
            .setContentIntent(getMainActivityIntent())
            .build()
    }

    private fun getPlayPendingIntent(): PendingIntent {
        val intent = Intent(context, AudioService::class.java).apply {
            action = "com.leandromendes.vehicleequalizer.PLAY"
        }
        return PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getPausePendingIntent(): PendingIntent {
        val intent = Intent(context, AudioService::class.java).apply {
            action = "com.leandromendes.vehicleequalizer.PAUSE"
        }
        return PendingIntent.getService(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getMainActivityIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
