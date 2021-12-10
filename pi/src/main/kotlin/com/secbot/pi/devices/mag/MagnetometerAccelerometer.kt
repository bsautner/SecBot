package com.secbot.pi.devices.mag


import com.secbot.core.AbstractDevice
import com.secbot.core.Source
import com.secbot.core.DeviceListener
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.devices.C
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Math.PI


/**
 * ACC:X=0.268 Y=-0.727 Z=10.518
 * MAG:X=-9.818 Y=9.091 Z=-80.000
 */
object MagnetometerAccelerometer : AbstractDevice() {


    private var heading: Int = -5

    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)
        scope.async {
            while (stopped.not()) {
                readMag()
                deviceListener.onReceive("${Source.CMP.name},$heading")

                delay(100)
            }
        }.start()
    }

    private fun readMag() {

        val process = Runtime.getRuntime().exec("python3 /home/pi/python/mag.py")
        val bufferedReader = BufferedReader(
            InputStreamReader(process.inputStream)
        )

        bufferedReader.lines().forEach {


            val split = it.split(',')
            if (Source.MAG.matches(split[0])) {
                val magX = split[1].toFloat()
                val magY = split[2].toFloat()
                var newHeading: Int = (kotlin.math.atan2(magY, magX) * 180 / PI).toInt()
                if (newHeading < 0) {
                    newHeading += 360
                }

                if (MQTT.isConnected()) {
                    if ((newHeading > heading + 5) or (newHeading < heading - 5)) {
                        heading = newHeading
                        scope.async {
                            C.print("New Heading $heading")
                            MQTT.publish("CMP,$heading")
                        }.start()

                    }
                }


            }
        }
    }
}