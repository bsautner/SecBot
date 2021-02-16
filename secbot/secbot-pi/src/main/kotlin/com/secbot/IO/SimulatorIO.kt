package com.secbot.IO

import com.pi4j.io.serial.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimulatorIO(private val serial: Serial) : IO {
    override fun sendCommand(command: String) {
        println("Sending Simulated Command: $command")
    }

    override fun serialReceiver(scope: CoroutineScope, data: ReceiveChannel<String>): ReceiveChannel<String> = scope.produce {

        for (s in data) {
            send(s)
        }
    }


    override fun start(scope: CoroutineScope) = scope.produce {
        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {

                send(it.asciiString)
            }


        })

        scope.runCatching {

            while (true) {

                delay(10)
            }
        }
    }
}