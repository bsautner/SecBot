package com.secbot.pi.io

import com.pi4j.io.serial.*
import com.secbot.core.hardware.Control
import com.secbot.core.SensorDataHandler
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.hardware.Sensor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce


@ExperimentalCoroutinesApi
class SimulatorSensorDataHandler(private val serial: Serial) : SensorDataHandler {

     fun send(command: SensorData) {
        println("Sending simulated command $command")
    }

     fun receiveSensorData(data: SensorData) {
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



    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SensorData>): ReceiveChannel<SensorData> = scope.produce {

        for (s in data) {
            send(s)
        }
    }


    override fun start(scope: CoroutineScope) = scope.produce {
        serial.addListener(SerialDataEventListener {

            scope.launch(Dispatchers.IO) {

                send(SensorData(Sensor.SCANNING_IR, 0.0))
            }


        })

        scope.runCatching {

            while (true) {

                delay(10)
            }
        }
    }
}