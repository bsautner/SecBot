package com.secbot.core.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.Motion
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.async

object Motor : AbstractDevice<Motion>()  {

    var lastMotion: Motion = Motion.STOP


    override fun update(payload: Motion) {
        if (lastMotion != payload) {
            lastMotion = payload
            scope.async {
              //  MQTT.publish(Motor, "MOTOR,${payload}")
            }.start()

        }
    }

}