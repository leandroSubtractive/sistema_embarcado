package com.leandromendes.vehicleequalizer

import android.content.Intent
import com.leandromendes.vehicleequalizer.modules.notification.NotificationInterface
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackInterface
import com.leandromendes.vehicleequalizer.service.AudioService
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AudioServiceTest {

    private lateinit var service: AudioService
    private lateinit var mockPlayback: PlaybackInterface
    private lateinit var mockNotification: NotificationInterface

    @Before
    fun setup() {
        service = AudioService()

        mockPlayback = mock(PlaybackInterface::class.java)
        mockNotification = mock(NotificationInterface::class.java)

        // Injetando mocks via reflexão
        val playbackField = AudioService::class.java.getDeclaredField("playbackModule")
        playbackField.isAccessible = true
        playbackField.set(service, mockPlayback)

        val notifField = AudioService::class.java.getDeclaredField("notificationModule")
        notifField.isAccessible = true
        notifField.set(service, mockNotification)

        // Também precisamos inicializar o trackList (senão índice pode falhar)
        val trackIndexField = AudioService::class.java.getDeclaredField("currentTrackIndex")
        trackIndexField.isAccessible = true
        trackIndexField.set(service, 0)
    }

    @Test
    fun `quando receber ACTION_PLAY deve chamar play no Playback e showNotification`() {
        val intent = Intent().apply { action = MusicConstants.ACTION_PLAY }

        service.onStartCommand(intent, 0, 0)

        verify(mockPlayback).play()
        verify(mockNotification).showNotification(eq("playing"), anyString())
    }

    @Test
    fun `quando receber ACTION_PAUSE deve chamar pause no Playback e showNotification`() {
        val intent = Intent().apply { action = MusicConstants.ACTION_PAUSE }

        service.onStartCommand(intent, 0, 0)

        verify(mockPlayback).pause()
        verify(mockNotification).showNotification(eq("paused"), anyString())
    }

    @Test
    fun `quando receber ACTION_SEEK_TO deve chamar seekTo no Playback`() {
        val intent = Intent().apply {
            action = MusicConstants.ACTION_SEEK_TO
            putExtra(MusicConstants.EXTRA_SEEK_POSITION, 5000)
        }

        service.onStartCommand(intent, 0, 0)

        verify(mockPlayback).seekTo(5000)
    }

    @Test
    fun `quando receber ACTION_NEXT deve trocar de faixa e chamar play`() {
        val intent = Intent().apply { action = MusicConstants.ACTION_NEXT }

        service.onStartCommand(intent, 0, 0)

        verify(mockPlayback).setRawDataSource(anyInt())
        verify(mockPlayback).play()
        verify(mockNotification).showNotification(eq("playing"), anyString())
    }

    @Test
    fun `quando receber ACTION_PREVIOUS deve trocar de faixa e chamar play`() {
        val intent = Intent().apply { action = MusicConstants.ACTION_PREVIOUS }

        service.onStartCommand(intent, 0, 0)

        verify(mockPlayback).setRawDataSource(anyInt())
        verify(mockPlayback).play()
        verify(mockNotification).showNotification(eq("playing"), anyString())
    }

    @Test
    fun `onDestroy deve liberar playback e cancelar notificacao`() {
        service.onDestroy()

        verify(mockPlayback).release()
        verify(mockNotification).cancelNotification()
    }
}
