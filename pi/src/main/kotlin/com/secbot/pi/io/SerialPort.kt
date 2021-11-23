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
class SerialPort  (
    private val processor: SensorDataProcessor,
    private val serial: Serial,
    private val mqtt: MQTT,
    private val tty: String,
    private val gson: Gson) {


    suspend fun start() {

        println("starting serial $tty on thread: ${Thread.currentThread().name}")
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
        println("Started Feedback Port")


        serial.addListener(SerialDataEventListener {

            val deviceContainer = DeviceContainer()



            if (it.asciiString.isNotEmpty()) {
 
                println(it.asciiString.trim())


                try {


                    val split = it.asciiString.split('\n')
                    for (s in split) {
                        if (s.isNotBlank()) {
                            try {
                              //  deviceContainer.put(processor.process(s))
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
//                    GlobalScope.launch {
//                        mqtt.publishSensorData(deviceContainer)
//                    }
                }

            }
        })

    }
    suspend fun send(command: String) {

        if (serial.isOpen) {
            kotlin.runCatching {    serial.writeln( command) }

        } else {
            println("Can't send serial command because port is closed")
        }
    }

    fun isConnected(): Boolean {
        return serial.isOpen
    }



}


