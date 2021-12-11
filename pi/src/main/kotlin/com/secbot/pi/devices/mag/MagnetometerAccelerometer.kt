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
import java.lang.Math.atan2
import kotlin.math.roundToInt


/**
 * ACC:X=0.268 Y=-0.727 Z=10.518
 * MAG:X=-9.818 Y=9.091 Z=-80.000
 */
object MagnetometerAccelerometer : AbstractDevice() {


    private var heading = mutableMapOf(Pair(Source.MAG_SERIAL, 0), Pair(Source.MAG_PI, 0))

    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)
        scope.async {
            while (stopped.not()) {
                readPython()
                delay(1000)
            }
        }.start()
    }

    internal fun process(data: String) {
        val split = data.split(',')
        val source = Source.valueOf(split[0])
        if (heading.containsKey(source)) {
            println("MAG PROC $data")
            val magX = split[1].toFloat()
            val magY = split[2].toFloat()
            var newHeading: Int = (360 * kotlin.math.atan2(magY, magX) / PI).roundToInt()
            if (newHeading < 0) {
                newHeading += 360
            }
            println("$data:::: $newHeading")


                if ((newHeading > (heading[source]?.plus(5) ?: -1)).or(newHeading < (heading[source]?.minus(5) ?: -1))) {

                 //   heading[source] = newHeading
//                    scope.async {
//
//                    //MQTT.publish("$data,$heading")
//                        heading[source] = newHeading
//                //        C.print("New Heading $data $source $heading")
//
//                    }.start()

                }
                    }
    }

    private suspend fun readPython() {
        val process = Runtime.getRuntime().exec("python3 /home/pi/python/mag.py")
        val bufferedReader = BufferedReader(
            InputStreamReader(process.inputStream)
        )

        bufferedReader.lines().forEach {

            scope.async {

                deviceListener.onReceive(it)
             }.start()

        }
        runCatching {
            bufferedReader.close()
        }

    }
}