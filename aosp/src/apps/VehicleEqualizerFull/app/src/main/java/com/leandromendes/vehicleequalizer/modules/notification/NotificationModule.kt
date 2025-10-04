package com.leandromendes.vehicleequalizer.modules.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.service.AudioService
import com.leandromendes.vehicleequalizer.util.Constants.PlaybackStates
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants

class NotificationModule(private val context: Context) : NotificationInterface {

    companion object {
        const val CHANNEL_ID = "music_playback_channel"
        const val NOTIFICATION_ID = 1 // Arbitrary value
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Playback Controls",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    override fun buildNotification(playbackState: String, trackTitle: String): Notification {
        createNotificationChannel()

        val playPauseAction = if (playbackState == PlaybackStates.PLAYING) {
            NotificationCompat.Action(
                R.drawable.ic_pause,
                "Pause",
                getPendingIntent(MusicConstants.ACTION_PAUSE)
            )
        } else {
            NotificationCompat.Action(
                R.drawable.ic_play_arrow,
                "Play",
                getPendingIntent(MusicConstants.ACTION_PLAY)
            )
        }

        val stopAction = NotificationCompat.Action(
            R.drawable.ic_stop,
            "Stop",
            getPendingIntent(MusicConstants.ACTION_STOP)
        )

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, Class.forName("com.leandromendes.vehicleequalizer.MainActivity")),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(trackTitle)
            .setContentText(playbackState)
            .setSmallIcon(R.drawable.ic_music_note)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_music_note))
            .addAction(playPauseAction)
            .addAction(stopAction)
            .setContentIntent(contentIntent)
            .setOngoing(playbackState == PlaybackStates.PLAYING)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(context, AudioService::class.java).apply { this.action = action }
        return PendingIntent.getService(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun showNotification(playbackState: String, trackTitle: String) {
        createNotificationChannel()
        val notification = buildNotification(playbackState, trackTitle)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun updateNotification(playbackState: String, trackTitle: String) {
        val notification = buildNotification(playbackState, trackTitle)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
