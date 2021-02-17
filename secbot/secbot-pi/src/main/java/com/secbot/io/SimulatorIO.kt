package com.secbot.io

import com.pi4j.io.serial.*
import com.secbot.control.Device
import com.secbot.control.IO
import com.secbot.control.SerialData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimulatorIO(private val serial: Serial) : IO {

    override fun sendCommand(command: SerialData) {
        println("Sending simulated command $command")
    }


//    override fun commandReceiver(scope: CoroutineScope, data: ReceiveChannel<String>): ReceiveChannel<String> = scope.produce {
//
//        scope.runCatching {
//            for (s in data) {
//                serial.write("$s\n")
//            }
//        }
//
//    }


    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

        for (s in data) {
            send(s)
        }
    }


    override fun start(scope: CoroutineScope) = scope.produce {
        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {

                send(SerialData(Device.DEBUG, 0.0, System.currentTimeMillis()))
            }


        })

        scope.runCatching {

            while (true) {

                delay(10)
            }
        }
    }
}