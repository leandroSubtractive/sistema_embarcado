package com.leandromendes.vehicleequalizer.modules.notification

import android.content.Context
import androidx.media3.session.MediaSession
import android.app.Service

interface NotificationInterface {
    fun showNotification(playbackState: String)
    fun updateNotification(playbackState: String)
    fun cancelNotification()
}
