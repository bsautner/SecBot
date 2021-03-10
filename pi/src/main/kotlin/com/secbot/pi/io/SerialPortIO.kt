package com.secbot.pi.io

import com.pi4j.io.serial.*
import com.secbot.core.Device
import com.secbot.core.IO
import com.secbot.core.SerialData
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.lang.Exception
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class SerialPortIO( private val serial: Serial) : IO {
    private val tty = "ttyUSB0"

    private val sb = StringBuilder()

    override fun send(command: SerialData) {

        if (serial.isOpen) {
            serial.write(command.toSerialCommand())
        } else {
            println("Can't send serial command because port is closed")
        }
    }

    override fun receive(data: SerialData) {
        TODO("Not yet implemented")
    }

    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

        for (s in data) {
         //   println(s)
            send(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {

        println("starting serial on thread: ${Thread.currentThread().name}")

        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {

                if (it.asciiString.isNotEmpty()) {
                     val sanitized = it.asciiString

                    if (sanitized.startsWith(Device.DEBUG.name)) {
                        //println(sanitized)
                    } else {

                        try {

                            for (s in sanitized.toCharArray()) {


                                when {
                                    (s == '{')  -> {
                                        sb.clear()
                                    }
                                    (s == '}')  -> {
                                        val split = sb.toString().split(":")
                                        send(
                                            SerialData(
                                                Device.valueOf(split[0]),
                                                split[1].toDouble(),
                                                split[2].toLong()
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
                            for (c in sanitized.toCharArray()) {
                                println(c)
                            }
                            ex.printStackTrace()

                        }
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
