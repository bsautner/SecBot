package com.secbot.core.devices.lidar

import com.secbot.core.AbstractDevice
import com.secbot.core.Source
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.async
import java.util.concurrent.atomic.AtomicInteger

object Lidar : AbstractDevice() {


    val data = HashMap<Int, AtomicInteger>()

    init {
        for (i in 0..360) {
            data[i] = AtomicInteger(0)
        }
    }


    fun update(split: List<String>) {
        val angle = split[1].toBigDecimal().toInt()
        val distance =  split[2].toBigDecimal().toInt()
        data[angle]?.set(distance)
        send(angle, distance)


    }

    private fun send(angle: Int, distance: Int) {
        scope.async {
            MQTT.publish("${Source.LDR},$angle,$distance")
        }.start()
    }


}