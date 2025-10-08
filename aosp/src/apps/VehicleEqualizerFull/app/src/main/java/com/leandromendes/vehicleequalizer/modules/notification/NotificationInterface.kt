package com.leandromendes.vehicleequalizer.modules.notification

import android.app.Notification

/**
 * Notification interface
 *
 * @constructor Create empty Notification interface
 */
interface NotificationInterface {

    /**
     * Build notification
     *
     * @param playbackState Current status of the player
     * @param trackTitle Audio track name
     * @return Notification class object
     */
    fun buildNotification(playbackState: String, trackTitle: String = "No music"): Notification

    /**
     * Show notification
     *
     * @param playbackState Current status of the player
     * @param trackTitle Audio track name
     */
    fun showNotification(playbackState: String, trackTitle: String = "No music")

    /**
     * Update notification
     *
     * @param playbackState Current status of the player
     * @param trackTitle Audio track name
     */
    fun updateNotification(playbackState: String, trackTitle: String = "No music")

    /**
     * Cancel notification
     *
     */
    fun cancelNotification()
}
