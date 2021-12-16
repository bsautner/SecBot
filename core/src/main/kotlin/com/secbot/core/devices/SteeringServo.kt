package com.secbot.core.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.Source
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.async

object SteeringServo : AbstractDevice() {

    enum class Direction {
       LEFT, RIGHT, FORWARD
    }

    fun turn(pos: Direction) {
        scope.async {
            MQTT.publish("${Source.STEER},$pos")
        }.start()

    }

}