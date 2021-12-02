package com.secbot.core.mqtt

import com.google.gson.GsonBuilder
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
    private val broker = "tcp://54.167.121.102:1883"
    private val clientId = UUID.randomUUID().toString()
    private val persistence = MemoryPersistence()
    private val client = MqttClient(broker, clientId, persistence)
    private val gson = GsonBuilder().create()



    fun start() {
        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = true
        connOpts.userName = "ben"
        connOpts.password = "$".toCharArray()
        println("Connecting to broker: $broker")
        client.connect(connOpts)
        println("Connected MQTT OK")
    }

    fun publish(data: String) {

        checkConnection()
        val payload = Payload(data)
        val message = MqttMessage(gson.toJson(payload).toByteArray())
        message.qos = qos
        client.publish(topic, message)
         println("published [$payload] to $topic")

    }

    fun subscribe(callback: MqttCallback) {
        checkConnection()
        client.subscribe(topic)
        client.setCallback(callback)

    }

    private fun checkConnection() {
        if (! client.isConnected ) {
            println("reconnecting mqtt broker")
            start()
        }
    }

    companion object {
        private const val topic = "topic"

    }



}