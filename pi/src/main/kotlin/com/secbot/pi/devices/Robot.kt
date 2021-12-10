package com.secbot.pi.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.DeviceListener
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.devices.audio.Speaker
import com.secbot.pi.devices.camera.Camera
import com.secbot.pi.devices.mag.MagnetometerAccelerometer
import com.secbot.pi.devices.serial.SerialPort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
object Robot : AbstractDevice() {

    private val devices: MutableList<AbstractDevice> = ArrayList()


    @Throws(IOException::class, InterruptedException::class)
    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)


        scope.async {


                C.print("starting devices")

                devices.add(MQTT)
                devices.add(SerialPort)
                devices.add(Camera)
                devices.add(Speaker)
                devices.add(MagnetometerAccelerometer)

                devices.forEach {
                    it.start(deviceListener)
                }


        }.start()


        while (stopped.not()) {
            delay(1000)
            SerialPort.send("ping")
        }


        println("exiting...")
    }

    override fun stop() {
        devices.forEach {
            it.stop()
        }
    }


}


