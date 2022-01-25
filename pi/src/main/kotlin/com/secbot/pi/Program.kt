@file:OptIn(KtorExperimentalAPI::class)

package com.secbot.pi

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.secbot.core.DeviceScope
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.devices.lidar.LidarPoint
import com.secbot.core.mqtt.MQTT
import com.secbot.core.mqtt.MqttListener
import io.ktor.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.IOException

object Program {

    private const val broker =  "tcp://localhost:1883"
    private val gson = Gson()
    private val scope = DeviceScope()
    private val lidar = Lidar()

    private val listener = object : MqttListener {
        override fun onConnected() {
            println("MQTT Connected")
            mqtt.subscribe("#")
          //  mqtt.subscribe("RAW_LIDAR")

        }

        override fun connectionLost(cause: Throwable?) {
           println("MQTT Connection Lost: ${cause?.message}")
            cause?.printStackTrace()
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            scope.launch {
                when (topic) {
                    "RAW_LIDAR" -> {
                        message?.let {
                            val payload = gson.fromJson(String(it.payload), JsonArray::class.java)
                            payload.forEach { v ->
                                val item = v.asJsonArray
                                if (item[0].asInt >= 15 && item[2].asDouble > 5.0) {
                                    val point = LidarPoint(item[1].asDouble, item[2].asDouble)
                                    if (lidar.update(point)) {
                                     //   mqtt.publish(Lidar.topic, gson.toJson(point))
                                    }
                                }
                            }
                        }
                        //  println("Processing Raw Lidar")
                    }
                    else -> {
                        println("${System.currentTimeMillis()}  MQTT Message Arrived but unhandled $topic")
                    }
                }
            }


        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) { }

    }

    private val mqtt : MQTT = MQTT(listener, broker)


    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        GlobalScope.launch {
            mqtt.start()
        }


        while (true) {
            Thread.sleep(10)
        }

    }


}


