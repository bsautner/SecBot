package com.secbot.pi.io

import com.google.gson.Gson
import com.pi4j.io.serial.*
import com.secbot.core.data.DeviceCommand
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

@ExperimentalCoroutinesApi
class DeviceSerialPortManager(private val serial: Serial, private val mqtt: MQTT, tty: String, private val gson: Gson) : SerialPort(serial, tty) {




    override suspend fun start() {
        super.start()
        subscribeDeviceCommands()

    }

    private fun subscribeDeviceCommands() {

        mqtt.subscribeDeviceCommands(object: MqttCallback {
            override fun connectionLost(cause: Throwable) {

                println("MQTT Connection lost")
                cause.printStackTrace()
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                println("MQTT Message Arrived $topic ${String(message.payload)}")
                GlobalScope.launch {
                    sendCommand(gson.fromJson(String(message.payload), DeviceCommand::class.java))
                }

            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {

            }

        })
    }


    override suspend fun sendCommand(command: DeviceCommand) {

        if (serial.isOpen) {
            kotlin.runCatching {    serial.writeln(gson.toJson(command)) }

        } else {
            println("Can't send serial command because port is closed")
        }
    }

}
