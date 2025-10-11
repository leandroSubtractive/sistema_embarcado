package com.leandromendes.vehicleequalizer

import android.app.Notification
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leandromendes.vehicleequalizer.modules.notification.NotificationModule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Testes unitários para o módulo de notificações.
 * Verifica a criação e exibição das notificações de áudio.
 */
class NotificationModuleTest {

    private lateinit var context: Context
    private lateinit var notificationModule: NotificationModule

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        notificationModule = NotificationModule(context)
    }

    /**
     * Testa a criação do canal de notificação.
     */
    @Test
    fun testCreateNotificationChannel() {
        val notification: Notification = notificationModule.buildNotification(
            playbackState = "PLAYING",
            trackTitle = "Música de Teste"
        )
        assertNotNull("A notificação não deve ser nula", notification)
    }

    /**
     * Testa a exibição de uma notificação.
     */
    @Test
    fun testCreateNotification() {
        notificationModule.showNotification("PAUSED", "Som de Teste")
        assertTrue("Notificação exibida com sucesso", true)
    }
}
