package com.secbot.core.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.Motion
import java.io.File

object Sonar  : AbstractDevice<Float>() {

    lateinit var process: Process
    var rangeCm : Float = 0.0F

    override fun update(payload: Float) {
        rangeCm = payload
        if (payload < 5F) {
            Motor.update(Motion.STOP)
        }

    }

    override fun start() {
        val path = "/home/pi/sonar/sonar.py"
        val file = File(path)
        if (file.exists()) {
            process = Runtime.getRuntime().exec("python3 $path")
        }
    }

    override fun stop() {
        super.stop()
        process.destroy()
    }
}