package com.secbot.pi.devices

import com.secbot.core.AbstractDevice
import com.secbot.core.DeviceListener
import com.secbot.core.Source
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.devices.audio.Speaker
import com.secbot.pi.devices.camera.Camera
import com.secbot.pi.devices.led.ArduinoPongLed
import com.secbot.core.devices.lidar.Lidar
import com.secbot.pi.devices.mag.MagnetometerAccelerometer
import com.secbot.pi.devices.serial.SerialPort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
object Robot : AbstractDevice() {

    private val devices: MutableList<AbstractDevice> = ArrayList()
    private val broker = "tcp://localhost:1883"

    @Throws(IOException::class, InterruptedException::class)
    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)
        MQTT.broker = broker

        scope.async {


                C.print("starting devices")

                devices.add(MQTT)
                devices.add(Camera)
                devices.add(Speaker)
                devices.add(MagnetometerAccelerometer)
                devices.add(SerialPort)
                devices.add(ArduinoPongLed)
                devices.add(Lidar)

                devices.forEach {
                    it.start(deviceListener)
                }


        }.start()


        while (stopped.not()) {
            delay(1000)

        }


        println("exiting...")
    }

    override fun stop() {
        devices.forEach {
            it.stop()
        }
    }


}


