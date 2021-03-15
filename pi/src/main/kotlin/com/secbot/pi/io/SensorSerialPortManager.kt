package com.secbot.pi.io

import com.google.gson.Gson
import com.pi4j.io.serial.*
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class SensorSerialPortManager  (
    private val serial: Serial,
    private val mqtt: MQTT,
    private val tty: String,
    private val gson: Gson) : SerialPort(serial, tty) {

    private val sb = StringBuilder()

    override suspend fun start() {
        super.start()


        serial.addListener(SerialDataEventListener {

            if (it.asciiString.isNotEmpty()) {
                val sanitized = it.asciiString

                try {

                    for (s in sanitized.toCharArray()) {
                        sb.append(s)
                        when {
                            (s == '}') -> {
                                val sensorData = gson.fromJson(sb.toString(), SensorData::class.java)
                                println("Got Sensor Data over Serial Port $tty $sb")
                                GlobalScope.launch {
                                    mqtt.publishSensorData(sensorData)
                                }

                                sb.clear()
                            }
                        }


                    }


                } catch (ex: Exception) {
                    println("Malformed Serial Data : $sanitized caused ${ex.message}")
                    ex.printStackTrace()

                }

            }
        })

    }

    override suspend fun sendCommand(command: DeviceCommand) {

    }


}


