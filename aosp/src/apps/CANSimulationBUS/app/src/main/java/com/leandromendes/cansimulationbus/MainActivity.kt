package com.leandromendes.cansimulationbus

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.leandromendes.cansimulationbus.data.model.CanMessage
import com.leandromendes.cansimulationbus.util.VehicleCanBusSimulator
import com.leandromendes.cansimulationbus.util.VehicleSensorSimulator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var speedLabel: TextView // Novo TextView para velocidade
    private lateinit var readSpeedButton: Button // Novo Botão para velocidade

    private lateinit var canVolumeLabel: TextView // Novo TextView para volume CAN
    private lateinit var sendCanVolumeButton: Button // Novo Botão para volume CAN

    private val TAG = "CANSimulationBus"

    // Instância do simulador de sensor de velocidade
    private val vehicleSensorSimulator = VehicleSensorSimulator("Speed")
    private val vehicleCanBusSimulator = VehicleCanBusSimulator() // Instância do simulador CAN
    private val activityScope = CoroutineScope(Dispatchers.Main) // Escopo para corrotinas da Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializa o TextView
        speedLabel = findViewById(R.id.speedLabel)
        // Inicializa o Botão
        readSpeedButton = findViewById(R.id.readSpeedButton)

        // Inicializa o TextView CAN
        canVolumeLabel = findViewById(R.id.canVolumeLabel)
        // Inicializa o Botão CAN
        sendCanVolumeButton = findViewById(R.id.sendCanVolumeButton)

        readSpeedButton.setOnClickListener {
            val currentSpeed = vehicleSensorSimulator.readSensorData()
            speedLabel.text = "Velocidade Atual: $currentSpeed km/h"
            Log.d(TAG, "Velocidade lida: $currentSpeed km/h")
        }

        // Listener para o botão de envio de volume CAN
        sendCanVolumeButton.setOnClickListener {
            // Simula o envio de uma mensagem CAN de volume (ID 0x123, valor aleatório 0-100)
            val randomVolume = (0..100).random()
            val message = CanMessage(id = 0x123, data = byteArrayOf(randomVolume.toByte()))
            vehicleCanBusSimulator.sendMessage(message)
        }

        // Coleta mensagens CAN recebidas e atualiza a UI
        activityScope.launch {
            vehicleCanBusSimulator.canMessageFlow.collect { message ->
                if (message.id == 0x123 && message.data.isNotEmpty()) {
                    val volume = message.data[0].toInt() and 0xFF
                    canVolumeLabel.text = "Volume CAN: $volume"
                    // Em um cenário real, você poderia usar este volume para ajustar o áudio
                    // equalizerService?.setMasterVolume(volume)
                    // Exemplo de chamada ao serviço
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Importante: parar o simulador CAN quando a atividade for
        // destruída ou não for mais necessária
        vehicleCanBusSimulator.stopSimulator()
        activityScope.cancel() // Cancela as corrotinas do escopo da atividade
    }
}