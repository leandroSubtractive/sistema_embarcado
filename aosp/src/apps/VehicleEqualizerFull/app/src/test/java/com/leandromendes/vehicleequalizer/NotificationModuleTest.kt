package com.leandromendes.vehicleequalizer.modules.notification

import android.app.Notification
import android.content.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Testes unitários para NotificationModule.
 * Simula comportamento sem Android real usando Mockito.
 */
class NotificationModuleTest {

    @Mock private lateinit var context: Context
    private lateinit var module: NotificationModule

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        module = mock(NotificationModule::class.java, withSettings().defaultAnswer(CALLS_REAL_METHODS))
    }

    @Test
    fun testBuildNotification() {
        val notification: Notification = mock(Notification::class.java)
        `when`(module.buildNotification(anyString(), anyString())).thenReturn(notification)

        val result = module.buildNotification("PLAYING", "Música de Teste")

        assert(result == notification)
        verify(module).buildNotification("PLAYING", "Música de Teste")
    }

    @Test
    fun testShowNotification() {
        doNothing().`when`(module).showNotification(anyString(), anyString())

        module.showNotification("PAUSED", "Som de Teste")

        verify(module).showNotification("PAUSED", "Som de Teste")
    }
}
