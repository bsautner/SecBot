package com.secbot.core.mqtt

import com.google.gson.GsonBuilder
import com.secbot.core.SensorDataHandler
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
class MQTT : SensorDataHandler {

    private val sensorTopic = "sensor"
    private val controlTopic = "control"

    private val qos = 2
    private val broker = "tcp://192.168.1.30:1883"
    private val clientId = UUID.randomUUID().toString()
    private val persistence = MemoryPersistence()
    private val client = MqttClient(broker, clientId, persistence)
    private val gson = GsonBuilder().create()
    var feedbackListener: FeedbackListener? = null


    init {
        val connOpts = MqttConnectOptions()
        connOpts.isCleanSession = true
        println("Connecting to broker: $broker")
        client.connect(connOpts)
        println("Connected MQTT OK")
    }

    fun publishSensorData(command: SensorData) {
        println("mqtt sending ${command.toString()}")
        val message = MqttMessage(gson.toJson(command).toByteArray())
        message.qos = qos
        client.publish(sensorTopic, message)
    }

//     fun receiveSensorData(data: SensorData) {
//      //  println("mqtt received ${data.toSerialCommand()}")
//        val message = MqttMessage(gson.toJson(data).toByteArray())
//        message.qos = qos
//      //  client.publish(controlTopic, message)
//    }

    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SensorData>): ReceiveChannel<SensorData> = scope.produce {

    for (s in data) {
           // println(s)
            send(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {

        client.subscribe(sensorTopic)
        client.setCallback(callback(this, feedbackListener))

//
//        client.subscribe(controlTopic)
//        client.setCallback(callback(this))

    }

    class callback(private val scope: ProducerScope<SensorData>, private val feedbackListener: FeedbackListener?) : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            cause?.printStackTrace()
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            println("MQTT Message Received $topic/$message")
            val json = message?.payload?.let { String(it) }
            val serialData = GsonBuilder().create().fromJson(json, SensorData::class.java)
            feedbackListener?.onRevieve(serialData)

            scope.launch(Dispatchers.IO) {


                scope.send(GsonBuilder().create().fromJson(json, SensorData::class.java))
            }

        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            println("MQTT Message SEnt ${token.toString()}")
        }

    }


}