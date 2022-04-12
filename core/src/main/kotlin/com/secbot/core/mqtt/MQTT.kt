package com.secbot.core.mqtt

import kotlinx.coroutines.delay
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*

class MQTT(private val listener: MqttListener, private val broker: String) {



    private val clientId = UUID.randomUUID().toString()
    private val persistence = MemoryPersistence()
    private lateinit var client : MqttClient



     suspend fun start() {
         client = MqttClient(broker, clientId, persistence)


            val connOpts = MqttConnectOptions()
            connOpts.isCleanSession = true
            connOpts.userName = "ben"
            connOpts.password = "imarobot".toCharArray()
            println("MQTT Connecting to broker: $broker")
            client.connect(connOpts)


            client.setCallback(listener)
            while (client.isConnected.not()) {
                delay(10)
            }
           listener.onConnected()

    }

     fun publish(topic: String, data: String) {
         println("MQTT TX $topic $data")

        val message = MqttMessage(data.toByteArray())
        message.qos = qos
         if (client.isConnected) {
             client.publish(topic, message)
         }


    }

     fun subscribe(topic: String) {
       // println("MQTT SUB $topic")
        client.subscribe(topic)


    }

    fun stop() {
        client.disconnect()
    }


    companion object {
        private const val qos = 0
    }

}