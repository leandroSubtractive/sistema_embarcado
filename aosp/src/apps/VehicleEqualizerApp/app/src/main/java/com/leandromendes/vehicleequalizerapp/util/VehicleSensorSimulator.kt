package com.leandromendes.vehicleequalizerapp.util

import android.util.Log
import java.util.Random

// Propiedade da classe
class VehicleSensorSimulator (private val sensorName: String){ // Construtor Primário

    private val TAG = "VehicleSensorSimulator"
    private val random = Random()

    /**
     * Simula a leitura de dados de um sensor do veículo.
     * @return Um valor inteiro simulando a leitura do sensor
     * (ex: velocidade em km/h, temperatura em °C).
     */
    fun readSensorData(): Int {

        val data = when (sensorName) {
            "Speed" -> random.nextInt(201) // 0-200km/h
            "Outside Temperature" -> random.nextInt(50) -10 // -10 a 39 °C
            "Fuel Level" -> random.nextInt(101) // 0 - 100%
            else -> random.nextInt(101)  // Valor genérico 0-100
        }
        Log.d(TAG, "[$sensorName] Lendo dados do sensor: $data")

        return data
    }

    /**
     * Simula uma operação de calibração para o sensor.
     */
    fun calibrateSensor() {

        Log.i(TAG, "[$sensorName] Calibrando sensor...")

        // Simula um processo de calibração que levaria tempo
        Thread.sleep(1000) // Simula um atraso de 1 segundo
        Log.i(TAG, "[$sensorName] Sensor calibrado.")
    }
}