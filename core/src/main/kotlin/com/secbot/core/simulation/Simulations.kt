package com.secbot.core.simulation

import com.secbot.core.hardware.Compass
import com.secbot.core.hardware.DeviceContainer
import com.secbot.core.hardware.Lidar
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

object Simulations {

    @JvmStatic
    fun main(args: Array<String>) {

        GlobalScope.launch {

            val mqtt = MQTT()
            val rand = Random()

            while (true) {
                for (i in 0..360) {
                    val deviceContainer = DeviceContainer()
                    deviceContainer.put(Compass(heading = i.toDouble()))
                    for (r in 0..90) {
                        deviceContainer.put(Lidar(distance = (100).toDouble(), angle = r.toDouble()))
                    }
                    for (r in 90..180) {
                        deviceContainer.put(Lidar(distance = (200).toDouble(), angle = r.toDouble()))
                    }
                    for (r in 180..270) {
                        deviceContainer.put(Lidar(distance = (300).toDouble(), angle = r.toDouble()))
                    }
                    for (r in 270..360) {
                        deviceContainer.put(Lidar(distance = (400).toDouble(), angle = r.toDouble()))
                    }
                    mqtt.publishSensorData(deviceContainer)


                    delay(100)
                }
            }

        }

        while(true) {

        }

    }
}