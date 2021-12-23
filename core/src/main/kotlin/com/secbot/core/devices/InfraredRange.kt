package com.secbot.core.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.Motion

object InfraredRange : AbstractDevice<Float>() {

    var rangeCm : Float = 0.0F

    override fun update(payload: Float) {
        rangeCm = payload
        if (payload < 20F) {
            Motor.update(Motion.STOP)
        }

    }


}