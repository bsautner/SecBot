package com.secbot.mqtt

import com.secbot.control.Device
import com.secbot.control.IO
import com.secbot.control.SerialData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

@ExperimentalCoroutinesApi
class MQTT : IO {

    private val topic = "test"
    private val qos = 2
    private val broker = "tcp://192.168.1.30:1883"
    private val clientId = "SecBot"
    private val persistence = MemoryPersistence()
    private val client = MqttClient(broker, clientId, persistence)

    init {
        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = true
        println("Connecting to broker: $broker")
        client.connect(connOpts)
        println("Connected MQTT OK")
    }



    override fun sendCommand(command: SerialData) {
        val message = MqttMessage(command.toSerialCommand().toByteArray())
        message.qos = qos
        client.publish(topic, message)
    }

    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

    for (s in data) {
            println(s)
            send(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {

        client.subscribe(topic)
        client.setCallback(callback(this))

    }

    class callback(private val scope: ProducerScope<SerialData>) : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            cause?.printStackTrace()
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            println("MQTT Message Received $topic/$message")
            scope.launch(Dispatchers.IO) {
                scope.send(SerialData(Device.SCANNING_IR, 0.0, System.currentTimeMillis()))
            }

        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            println("MQTT Message SEnt ${token.toString()}")
        }

    }


}