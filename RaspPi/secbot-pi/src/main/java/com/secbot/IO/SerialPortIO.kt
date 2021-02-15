package com.secbot.IO

import com.pi4j.io.serial.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

@ExperimentalCoroutinesApi
class SerialPortIO( private val serial: Serial) : IO {
    private val tty = "ttyACM0"


    override fun sendCommand(command: String) {
        kotlin.runCatching {
            serial.write("$command\n")
        }

    }



    override fun serialReceiver(scope: CoroutineScope, data: ReceiveChannel<String>): ReceiveChannel<String> = scope.produce {

        for (s in data) {
            send(s)
        }
    }


    override fun start(scope: CoroutineScope) = scope.produce {


        println("starting serial...")

        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {

                send(it.asciiString)
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

            while (true) {

                delay(10)
            }
        }


    }

}
