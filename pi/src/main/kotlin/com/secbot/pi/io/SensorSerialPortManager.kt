package com.secbot.pi.io

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pi4j.io.serial.*
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.lang.Exception
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class SensorSerialPortManager(
    private val serial: Serial,
    private val mqtt: MQTT,
    private val tty: String,
    private val gson: Gson) {

    private val sb = StringBuilder()

    suspend fun start() {

        println("starting serial $tty on thread: ${Thread.currentThread().name}")

        serial.addListener(SerialDataEventListener {

            if (it.asciiString.isNotEmpty()) {
                val sanitized = it.asciiString

                try {

                    for (s in sanitized.toCharArray()) {
                        sb.append(s)
                        when {
                            (s == '}') -> {
                                val sensorData = gson.fromJson(sb.toString(), SensorData::class.java)
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


        val config = SerialConfig()
        config.device("/dev/$tty")
            .baud(Baud._115200)
            .dataBits(DataBits._8)
            .parity(Parity.NONE)
            .stopBits(StopBits._1)
            .flowControl(FlowControl.NONE)
        serial.open(config)
        serial.flush()
        delay(100)
        println("$tty connected")

        while (serial.isOpen) {

            delay(10)
        }
    }


}


