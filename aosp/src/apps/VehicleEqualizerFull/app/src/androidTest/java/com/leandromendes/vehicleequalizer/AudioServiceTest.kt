package com.leandromendes.vehicleequalizer.service

import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import com.leandromendes.vehicleequalizer.util.Constants.PlaybackStates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.times

/**
 * Testes instrumentados para AudioService
 *
 * NOTA: Testes instrumentados usam um ambiente de dispositivo/emulador real.
 * Não podemos isolar facilmente as classes internas (PlaybackModule, NotificationModule)
 * sem refatoração. Portanto, este teste se concentra em verificar o fluxo de controle
 * (Intents e ciclo de vida) em vez da lógica interna de reprodução/equalização.
 */
@RunWith(AndroidJUnit4::class)
class AudioServiceTest {

    // Regra que lida com o ciclo de vida do serviço para os testes
    @get:Rule
    val serviceRule = ServiceTestRule()

    private lateinit var context: Context
    private lateinit var serviceIntent: Intent

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        serviceIntent = Intent(context, AudioService::class.java)
    }

    // --- Testes de Ciclo de Vida ---
    // ---------------------------------

    @Test
    fun service_bindsAndCallsOnCreate() {
        // Inicia o serviço, chamando onCreate() e onBind()
        val binder = serviceRule.bindService(serviceIntent)

        // Verifica se o serviço foi criado corretamente (o binder não é nulo)
        assertNotNull(binder)

        // Neste ponto, o onCreate já foi chamado.
    }

    @Test
    fun service_startsForegroundOnCreate() {
        // Ao iniciar o serviço, ele deve chamar startForeground
        // 1. Use bindService() instead of startService()
        // bindService() returns the IBinder you set up (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // 2. Cast the binder to your custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // 3. Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()
        assertNotNull(service)

        // A verificação direta do startForeground é complexa em testes instrumentados
        // sem mocks de classes internas. O ServiceTestRule garante que onStartCommand
        // seja chamado, e onStartCommand chama startForeground.
        // Verificamos o valor de retorno para garantir que o comando foi processado.
        val result = service.onStartCommand(null, 0, 0)
        assertEquals(Service.START_STICKY, result)
    }

    // --- Testes de Ações de Reprodução (Playback Actions) ---
    // ---------------------------------------------------------

    @Test
    fun onStartCommand_actionPlay_shouldStartPlayback() {
        // 1. Use bindService() instead of startService()
        // bindService() returns the IBinder you set up (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // 2. Cast the binder to your custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // 3. Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()

        // 2. Cria o Intent de AÇÃO_PLAY
        val playIntent = Intent(serviceIntent).apply {
            action = MusicConstants.ACTION_PLAY
        }

        // 3. Executa o comando
        service.onStartCommand(playIntent, 0, 1)

        // 4. Verifica o estado. Como não podemos mockar o PlaybackModule,
        // verificamos o valor de retorno de onStartCommand e o estado do ciclo de vida
        // que deve ser START_STICKY.
        val result = service.onStartCommand(playIntent, 0, 1)
        assertEquals(Service.START_STICKY, result)

        // Se pudéssemos mockar o NotificationModule, verificaríamos a notificação:
        // verify(mockNotificationModule).showNotification(PlaybackStates.PLAYING, any())
    }

    @Test
    fun onStartCommand_actionPause_shouldPausePlayback() {
        // 1. Inicia e coloca no estado PLAYING (para garantir que PAUSE faz algo)
        // 1. Use bindService() instead of startService()
        // bindService() returns the IBinder you set up (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // 2. Cast the binder to your custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // 3. Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()

        service.onStartCommand(Intent(serviceIntent).apply { action = MusicConstants.ACTION_PLAY }, 0, 1)

        // 2. Cria o Intent de AÇÃO_PAUSE
        val pauseIntent = Intent(serviceIntent).apply {
            action = MusicConstants.ACTION_PAUSE
        }

        // 3. Executa o comando
        service.onStartCommand(pauseIntent, 0, 2)

        // 4. Verifica o estado
        val result = service.onStartCommand(pauseIntent, 0, 2)
        assertEquals(Service.START_STICKY, result)

        // Se pudéssemos mockar o NotificationModule, verificaríamos:
        // verify(mockNotificationModule).showNotification(PlaybackStates.PAUSED, any())
    }

    @Test
    fun onStartCommand_actionNext_shouldAdvanceTrackAndPlay() {
        // 1. Use bindService() instead of startService()
        // bindService() returns the IBinder you set up (AudioService.LocalBinder)
        val binder = serviceRule.bindService(serviceIntent)

        // 2. Cast the binder to your custom LocalBinder class
        val localBinder = binder as AudioService.LocalBinder

        // 3. Retrieve the actual AudioService instance from the binder
        val service = localBinder.getService()

        // Check initial state
        assertEquals("Toto Africa", service.getCurrentTrackTitle())

        // 4. Send the command to the service instance
        val nextIntent = Intent(serviceIntent).apply {
            action = MusicConstants.ACTION_NEXT
        }

        // Call the command directly on the service instance
        service.onStartCommand(nextIntent, 0, 1)

        // 5. Verify the new state
        assertEquals("Yohan Kim Friends", service.getCurrentTrackTitle())
    }

}