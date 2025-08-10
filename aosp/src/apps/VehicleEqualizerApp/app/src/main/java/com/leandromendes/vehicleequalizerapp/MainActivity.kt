package com.leandromendes.vehicleequalizerapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.leandromendes.vehicleequalizerapp.util.VehicleSensorSimulator

class MainActivity : AppCompatActivity() {

    private lateinit var speedLabel: TextView // Novo TextView para velocidade
    private lateinit var readSpeedButton: Button // Novo Botão para velocidade

    private val TAG = "VehicleEqualizerApp"

    // Instância do simulador de sensor de velocidade
    private val vehicleSensorSimulator = VehicleSensorSimulator("Speed")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializa o TextView
        speedLabel = findViewById(R.id.speedLabel)
        // Inicializa o Botão
        readSpeedButton = findViewById(R.id.readSpeedButton)

        readSpeedButton.setOnClickListener {
            val currentSpeed = vehicleSensorSimulator.readSensorData()
            speedLabel.text = "Velocidade Atual: $currentSpeed km/h"
            Log.d(TAG, "Velocidade lida: $currentSpeed km/h")
        }
    }
}