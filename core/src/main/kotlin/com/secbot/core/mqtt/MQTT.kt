package com.secbot.core.mqtt

import com.google.gson.GsonBuilder
import com.secbot.core.AbstractDevice
import com.secbot.core.DeviceListener
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*

object MQTT : AbstractDevice() {
    private const val topic = "topic"
    private const val qos = 2
    private const val broker = "tcp://10.0.0.205:1883"
    private val clientId = UUID.randomUUID().toString()
    private val persistence = MemoryPersistence()
    private val client = MqttClient(broker, clientId, persistence)


    private val listener = object : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            println("MQTT Connection Lost")
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {

            message.let {
           //     println("MQTT Message Arrived $it")
                scope.async {
                    deviceListener.onReceive(it.toString())
                }
            }


        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {

        }

    }

    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)

        scope.async {
            val connOpts = MqttConnectOptions()
            connOpts.isCleanSession = true
            connOpts.userName = "ben"
            connOpts.password = "imarobot".toCharArray()
            println("MQTT Connecting to broker: $broker")
            client.connect(connOpts)
            println("MQTT Connected MQTT OK")

            delay(5000)
            subscribe(listener)

            while (stopped.not()) {
                delay(10)
            }
            println("MQTT Exiting Thread")
        }.start()

        while (client.isConnected.not()) {
            delay(10)
        }

    }

    fun isConnected() : Boolean {
        return client.isConnected
    }

    suspend fun publish(data: String) {

        checkConnection()

        val message = MqttMessage(data.toByteArray())
        message.qos = qos
        client.publish(topic, message)

    }

    private suspend fun subscribe(callback: MqttCallback) {
        checkConnection()
        client.subscribe(topic)
        client.setCallback(callback)

    }

    private suspend fun checkConnection() {
        if (! client.isConnected ) {
            println("MQTT reconnecting mqtt broker")
            start(deviceListener)
        }
    }




}