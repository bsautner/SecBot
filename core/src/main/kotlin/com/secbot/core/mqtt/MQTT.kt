package com.secbot.core.mqtt

import com.google.gson.GsonBuilder
import com.secbot.core.hardware.Device
import com.secbot.core.hardware.DeviceContainer
import com.secbot.core.hardware.DeviceType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*

@ExperimentalCoroutinesApi
class MQTT {



    private val qos = 2
    private val broker = "tcp://192.168.1.30:1883"
    private val clientId = UUID.randomUUID().toString()
    private val persistence = MemoryPersistence()
    private val client = MqttClient(broker, clientId, persistence)
    private val gson = GsonBuilder().create()

    fun start() {
        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = true
        println("Connecting to broker: $broker")
        client.connect(connOpts)
        println("Connected MQTT OK")
    }

    fun publishSensorData(deviceContainer: DeviceContainer) {

        checkConnection()
        val message = MqttMessage(gson.toJson(deviceContainer).toByteArray())
        message.qos = qos
        client.publish(sensorTopic, message)
    }

    fun publishDeviceCommand(command: Device) {
        checkConnection()
        client.publish(controlTopic, gson.toJson(command).toByteArray(), 2, false)
    }


    fun subscribeDeviceCommands(callback: MqttCallback) {
        checkConnection()
        client.subscribe(controlTopic)
        client.setCallback(callback)

    }

    fun subscribeSensorData(callback: MqttCallback) {
        checkConnection()
        client.subscribe(sensorTopic)
        client.setCallback(callback)

    }

    private fun checkConnection() {
        if (! client.isConnected ) {
            println("reconnecting mqtt broker")
            start()
        }
    }

    fun isConnected(): Boolean {
       return client.isConnected
    }

    companion object {
        private const val sensorTopic = "sensor"
        private const val controlTopic = "control"
    }



}