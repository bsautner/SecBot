package com.secbot.core.devices.lidar

import com.secbot.core.AbstractDevice
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.async
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Lidar : AbstractDevice<List<String>>() {


    val data = HashMap<Int, LidarPoint>()


    init {
        for (i in 1..(36000)) {
            data[i] = LidarPoint()

        }
    }


    override fun update(line: List<String>) {
        val index = line[1].toBigDecimal().times(BigDecimal(100)).toInt()

        data[index]?.let {
            it.timestamp = System.currentTimeMillis()
            it.angle = line[1].toBigDecimal().toDouble()
            it.distance = line[2].toBigDecimal().toDouble()

        }


    }




}