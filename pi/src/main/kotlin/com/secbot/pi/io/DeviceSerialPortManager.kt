package com.secbot.pi.io

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pi4j.io.serial.*
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.Exception
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class DeviceSerialPortManager(private val serial: Serial, private val mqtt: MQTT, private val tty: String, private val gson: Gson)  {




    suspend fun sendCommand(command: DeviceCommand) {

        if (serial.isOpen) {
            serial.write(gson.toJson(command))
        } else {
            println("Can't send serial command because port is closed")
        }
    }

    suspend fun subscribeDeviceCommands() {

        mqtt.subscribeDeviceCommands(object: MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                TODO("Not yet implemented")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                TODO("Not yet implemented")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                TODO("Not yet implemented")
            }

        })
    }



    suspend fun start() {



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
