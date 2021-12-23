package com.secbot.core.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.Source
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.async

object SteeringServo : AbstractDevice<SteeringServo.Direction>() {

    enum class Direction {
       LEFT, RIGHT, FORWARD
    }


    override fun update(payload: Direction) {
        scope.async {
            MQTT.publish(SteeringServo, "${Source.STEER},$payload")
        }.start()
    }

}