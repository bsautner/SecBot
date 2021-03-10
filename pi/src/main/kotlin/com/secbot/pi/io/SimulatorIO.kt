package com.secbot.pi.io

import com.pi4j.io.serial.*
import com.secbot.core.Device
import com.secbot.core.IO
import com.secbot.core.SerialData
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce


@ExperimentalCoroutinesApi
class SimulatorIO(private val serial: Serial) : IO {

    override fun send(command: SerialData) {
        println("Sending simulated command $command")
    }

    override fun receive(data: SerialData) {
        println("reveive simulated data $data")
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