package com.secbot.io

import com.pi4j.io.serial.*
import com.secbot.control.Device
import com.secbot.control.IO
import com.secbot.control.SerialData
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.lang.Exception

@ExperimentalCoroutinesApi
class SerialPortIO( private val serial: Serial) : IO {
    private val tty = "ttyACM1"

    override fun sendCommand(command: SerialData) {
       println("Sending Command ${command.toSerialCommand()}")
        if (serial.isOpen) {
            serial.write(command.toSerialCommand())
        } else {
            println("Can't send serial command because port is closed")
        }
    }


    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

        for (s in data) {

           // println("Serial Data Received ${s.toSerialCommand()}")
            send(s)
        }
    }


    override fun start(scope: CoroutineScope) = scope.produce {


        println("starting serial on thread: ${Thread.currentThread().name}")

        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {
             //   println("Serial Data Received ${it.asciiString}")
                if (it.asciiString.isNotEmpty()) {
                    val sanitized = it.asciiString.trimEnd('\n').trimEnd('\r').trimEnd('\n')

                    if (sanitized.startsWith(Device.DEBUG.name)) {
                        println(sanitized)
                    } else {
                        val split = sanitized.split(":")
                        try {

                            send(
                                SerialData(
                                    Device.valueOf(split[0].toUpperCase()),
                                    split[1].toDouble(),
                                    System.currentTimeMillis()
                                )
                            )
                        } catch (ex: Exception) {
                             println("Malformed Serial Data : $sanitized")
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

            while (serial.isOpen) {

                delay(10)
            }
        }


    }

}
