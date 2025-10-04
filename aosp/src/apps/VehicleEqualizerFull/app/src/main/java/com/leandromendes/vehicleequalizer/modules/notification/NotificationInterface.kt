package com.leandromendes.vehicleequalizer.modules.notification

import android.app.Notification

interface NotificationInterface {
    fun showNotification(playbackState: String, trackTitle: String = "No music")
    fun updateNotification(playbackState: String, trackTitle: String = "No music")
    fun cancelNotification()
    fun buildNotification(playbackState: String, trackTitle: String = "No music"): Notification
}
