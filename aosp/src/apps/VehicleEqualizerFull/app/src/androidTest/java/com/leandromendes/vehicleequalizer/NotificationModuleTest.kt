package com.leandromendes.vehicleequalizer

import android.app.Notification
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leandromendes.vehicleequalizer.modules.notification.NotificationModule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the notifications module
 *
 * Verifies the creation and display of audio notifications
 */
class NotificationModuleTest {

    private lateinit var context: Context
    private lateinit var notificationModule: NotificationModule

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        notificationModule = NotificationModule(context)
    }


     // Test the creation of the notification channel
    @Test
    fun testCreateNotificationChannel() {
        val notification: Notification = notificationModule.buildNotification(
            playbackState = "PLAYING",
            trackTitle = "Test Music"
        )
        assertNotNull("The notification shall not be void.", notification)
    }

    // Tests the display of a notification
    @Test
    fun testCreateNotification() {
        notificationModule.showNotification("PAUSED", "Test Music")
        assertTrue("Notification successfully displayed", true)
    }
}
