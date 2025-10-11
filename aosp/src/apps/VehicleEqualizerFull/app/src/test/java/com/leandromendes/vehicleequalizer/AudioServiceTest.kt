package com.leandromendes.vehicleequalizer.service

import android.content.Intent
import com.leandromendes.vehicleequalizer.modules.equalizer.EqualizerInterface
import com.leandromendes.vehicleequalizer.modules.notification.NotificationInterface
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackInterface
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import com.leandromendes.vehicleequalizer.util.Constants.PlaybackStates
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Testes unitários para o AudioService usando JUnit 5 + Mockito (sem Robolectric).
 * Aqui o construtor real do serviço não é chamado, evitando dependências Android.
 */
class AudioServiceTest {

    @Mock private lateinit var playbackModule: PlaybackInterface
    @Mock private lateinit var notificationModule: NotificationInterface
    @Mock private lateinit var equalizerModule: EqualizerInterface

    private lateinit var service: AudioService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        // Cria mock parcial do serviço, sem executar o construtor real (que chama Looper)
        service = mock(AudioService::class.java, withSettings().withoutAnnotations().defaultAnswer(CALLS_REAL_METHODS))

        // Injeta dependências mockadas via reflexão
        AudioService::class.java.getDeclaredField("playbackModule").apply {
            isAccessible = true
            set(service, playbackModule)
        }

        AudioService::class.java.getDeclaredField("notificationModule").apply {
            isAccessible = true
            set(service, notificationModule)
        }

        AudioService::class.java.getDeclaredField("equalizerModule").apply {
            isAccessible = true
            set(service, equalizerModule)
        }
    }

    @Test
    fun testPlayAction() {
        val intent = Intent(MusicConstants.ACTION_PLAY)
        service.onStartCommand(intent, 0, 0)

        verify(playbackModule).play()
        verify(notificationModule).showNotification(eq(PlaybackStates.PLAYING), anyString())
    }

    @Test
    fun testPauseAction() {
        val intent = Intent(MusicConstants.ACTION_PAUSE)
        service.onStartCommand(intent, 0, 0)

        verify(playbackModule).pause()
        verify(notificationModule).showNotification(eq(PlaybackStates.PAUSED), anyString())
    }

    @Test
    fun testStopAction() {
        val intent = Intent(MusicConstants.ACTION_STOP)
        service.onStartCommand(intent, 0, 0)

        verify(playbackModule).stop()
        verify(notificationModule).showNotification(eq(PlaybackStates.STOPPED), anyString())
    }

    @Test
    fun testEqualizerAction() {
        val intent = Intent(MusicConstants.ACTION_SET_BAND_LEVEL).apply {
            putExtra(MusicConstants.EXTRA_BAND, 2)
            putExtra(MusicConstants.EXTRA_LEVEL, 10)
        }
        service.onStartCommand(intent, 0, 0)
        verify(equalizerModule).setBandLevelSafe(2, 10)
    }
}
