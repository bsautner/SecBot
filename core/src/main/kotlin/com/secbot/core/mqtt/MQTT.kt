package com.secbot.core.mqtt

import com.secbot.core.AbstractDevice
import com.secbot.core.Bus
import com.secbot.core.Device
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*
import kotlin.collections.ArrayList

object MQTT : AbstractDevice<String>() {

    private const val qos = 0

    private val clientId = UUID.randomUUID().toString()
    private val persistence = MemoryPersistence()
    private lateinit var client : MqttClient
    lateinit var broker : String
    val topics : MutableList<Device> = ArrayList()

    private val listener = object : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            println("MQTT Connection Lost")
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {

            message?.let {
             println("MQTT RX $it")
                scope.launch {
                    Bus.post(it.toString())
                   }
            }


        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {

        }

    }

    override fun start() {
        super.start()
        client = MqttClient(broker, clientId, persistence)

        scope.launch {
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
            println("MQTT Connected MQTT OK")
               topics.forEach {
                   subscribe(it.topic())
               }

        }




    }

     fun publish(device: Device, data: String) {
         println("MQTT TX ${device.topic()} $data")
        checkConnection()

        val message = MqttMessage(data.toByteArray())
        message.qos = qos
        client.publish(device.topic(), message)


    }

     fun subscribe(topic: String) {
        checkConnection()

         println("MQTT SUB $topic")
        client.subscribe(topic)


    }

    private fun checkConnection() {
        if (! client.isConnected ) {
            println("MQTT reconnecting mqtt broker")
            start()
        }
    }

    override fun update(device: String) {
        TODO("Not yet implemented")
    }


}