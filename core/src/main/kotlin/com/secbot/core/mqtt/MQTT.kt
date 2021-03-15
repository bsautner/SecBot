package com.secbot.core.mqtt

import com.google.gson.GsonBuilder
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.*
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

    suspend fun start() {
        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = true
        println("Connecting to broker: $broker")
        client.connect(connOpts)
        println("Connected MQTT OK")
    }

    suspend fun publishSensorData(reading: SensorData) {
       // println("mqtt sending ${reading.toString()}")
        checkConnection()
        val message = MqttMessage(gson.toJson(reading).toByteArray())
        message.qos = qos
        client.publish(sensorTopic, message)
    }

    suspend fun publishDeviceCommand(command: DeviceCommand) {
        checkConnection()
        client.publish(controlTopic, gson.toJson(command).toByteArray(), 2, false)
    }


    suspend fun subscribeDeviceCommands(callback: MqttCallback) {
        checkConnection()
        client.subscribe(controlTopic)
        client.setCallback(callback)

    }

    suspend fun subscribeSensorData(callback: MqttCallback) {
        checkConnection()
        client.subscribe(sensorTopic)
        client.setCallback(callback)

    }

    suspend fun checkConnection() {
        if (! client.isConnected) {
            println("reconnecting mqtt broker")
            start()
        }
    }

    companion object {
        private const val sensorTopic = "sensor"
        private const val controlTopic = "control"
    }



}