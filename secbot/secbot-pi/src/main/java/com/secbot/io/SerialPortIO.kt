package com.secbot.io

import com.pi4j.io.serial.*
import com.secbot.control.Device
import com.secbot.control.IO
import com.secbot.control.SerialData
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.lang.Exception
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class SerialPortIO( private val serial: Serial) : IO {
    private val tty = "ttyUSB0"

    private val sb = StringBuilder()

    override fun sendCommand(command: SerialData) {

        if (serial.isOpen) {
            serial.write(command.toSerialCommand())
        } else {
            println("Can't send serial command because port is closed")
        }
    }

    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

        for (s in data) {
            println(s)
            send(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {

        var device : Device = Device.DEBUG
        var data: Double
        println("starting serial on thread: ${Thread.currentThread().name}")

        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {
             //   println("Serial Data Received ${it.asciiString}")
                if (it.asciiString.isNotEmpty()) {
                     val sanitized = it.asciiString//.trimEnd('\n').trimEnd('\r').trimEnd('\n')

                    if (sanitized.startsWith(Device.DEBUG.name)) {
                        println(sanitized)
                    } else {
                      //  val split = sanitized.trimStart('{').trimEnd('}').split(":")
                        try {

                            for (s in sanitized.toCharArray()) {

                                when {
                                    (s == '{')  -> {
                                        sb.clear()
                                    }
                                    (s == ':')  -> {
                                        device = Device.valueOf(sb.toString())
                                        sb.clear()
                                    }
                                    (s == '}')  -> {
                                        data = sb.toString().toDouble()
                                        send(
                                            SerialData(
                                                device,
                                                data,
                                                System.currentTimeMillis()
                                            )
                                        )
                                        sb.clear()
                                    }
                                    else -> {
                                        sb.append(s)
                                    }
                                }


                            }


                        } catch (ex: Exception) {
                             println("Malformed Serial Data : $sanitized caused ${ex.message}")
                            println("**************************************s")
                            for (s in sanitized.toCharArray()) {
                                println("$s : [${s.toInt()}]")
                            }
                            println("**************************************e")
                        }
                    }
                } else {
                    println("Malformed Serial Data : ${it.asciiString}")
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
            delay(100)

            while (serial.isOpen) {

                delay(10)
            }
        }


    }

}
