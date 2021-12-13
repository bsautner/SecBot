package com.secbot.core.devices.lidar

import com.secbot.core.AbstractDevice
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.async
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Lidar : AbstractDevice() {

    const val relevance = 100

    val data = HashMap<Int, LidarPoint>()


    init {
        for (i in 1..(360 * relevance)) {
            data[i] = LidarPoint(AtomicInteger(0))

        }
    }


    fun update(line: List<String>, mqttPub: Boolean) {
        val angle = line[1].toBigDecimal().times(BigDecimal(relevance)).toInt()
        val distance =  line[2].toBigDecimal().times(BigDecimal(relevance)).toInt()
        data[angle]?.distance?.set(distance)
        data[angle]?.timestamp?.set(System.currentTimeMillis())

        if (mqttPub) {
            scope.async {
                MQTT.publish(line.joinToString(separator = ",") { it })
            }.start()
        }


    }




}