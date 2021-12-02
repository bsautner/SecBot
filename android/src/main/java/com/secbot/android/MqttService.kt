package com.secbot.android

import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.eclipse.paho.client.mqttv3.MqttCallback

@ExperimentalCoroutinesApi
class MqttService(private val mqtt: MQTT, private val feedbackListener: MqttCallback) {


    suspend fun monitor() {
        try {
//            println("mqtt started")
//            mqtt.start()
//            delay(1000)
            mqtt.subscribe(feedbackListener)
            while (true) {
                delay(10)
            }
        } catch (ex: Exception) {
            println("BEN:: ${ex.message}")
            ex.printStackTrace()
        }
        println("BEN:: mqtt closed")
    }
}