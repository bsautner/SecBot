package com.secbot.pi.io

import com.google.gson.GsonBuilder
import com.pi4j.io.serial.*
import com.secbot.core.SensorDataHandler
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.lang.Exception
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class SerialPortManager(private val serial: Serial, private val mqtt: MQTT) : SensorDataHandler {
    private val tty = "ttyUSB0"

    private val sb = StringBuilder()
    private val gson = GsonBuilder().create()

    fun send(command: DeviceCommand) {

        if (serial.isOpen) {
            serial.write(gson.toJson(command))
        } else {
            println("Can't send serial command because port is closed")
        }
    }


    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SensorData>): ReceiveChannel<SensorData> = scope.produce {

        for (s in data) {
         //   println(s)
           mqtt.publishSensorData(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {

        println("starting serial on thread: ${Thread.currentThread().name}")

        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {

                if (it.asciiString.isNotEmpty()) {
                     val sanitized = it.asciiString

                        try {

                            for (s in sanitized.toCharArray()) {
                                sb.append(s)
                                when {
                                    (s == '}')  -> {
                                        val sensorData = gson.fromJson(sb.toString(), SensorData::class.java)
                                        send(sensorData)
                                        sb.clear()
                                    }
                                }



                            }


                        } catch (ex: Exception) {
                             println("Malformed Serial Data : $sanitized caused ${ex.message}")
//                            for (c in sanitized.toCharArray()) {
//                                println(c)
//                            }
                            ex.printStackTrace()

                        }

                }
            }


        })

        scope.runCatching {
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

            while (serial.isOpen) {

                delay(10)
            }
        }


    }

}
