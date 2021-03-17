package com.secbot.pi.io

import com.google.gson.Gson
import com.pi4j.io.serial.*
import com.secbot.core.SensorDataProcessor
import com.secbot.core.hardware.*
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import java.lang.NullPointerException
import java.util.*


@ExperimentalCoroutinesApi
class SensorSerialPortManager  (
    private val processor: SensorDataProcessor,
    private val serial: Serial,
    private val mqtt: MQTT,
    tty: String,
    private val gson: Gson) : SerialPort(serial, tty) {


    override suspend fun start() {
        super.start()


        serial.addListener(SerialDataEventListener {

            val deviceContainer = DeviceContainer()


            if (it.asciiString.isNotEmpty()) {



                try {


                    val split = it.asciiString.split('\n')
                    for (s in split) {
                        if (s.isNotBlank()) {
                            try {
                                deviceContainer.put(processor.process(s))
                            } catch (ex: NullPointerException) {
                               // println("FIXME - ${ex.message}")
                            }
                        }
                    }


                } catch (ex: Exception) {

                    println("Malformed Serial Data : ${it.asciiString} caused ${ex.message}")
                    ex.printStackTrace()

                }
                if (deviceContainer.isNotEmpty()) {
                    GlobalScope.launch {
                        mqtt.publishSensorData(deviceContainer)
                    }
                }

            }
        })

    }

    override suspend fun sendCommand(device: Device) {
        TODO("Not yet implemented")
    }


}


