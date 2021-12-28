package com.secbot.pi

import com.secbot.core.Bus
import com.secbot.core.DeviceScope
import com.secbot.pi.devices.C
import com.secbot.core.Robot
import com.secbot.core.devices.Motor
import com.secbot.core.devices.Sonar
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.devices.serial.SerialPort
import kotlinx.coroutines.*
import java.io.IOException

@ExperimentalCoroutinesApi
object Program  {

    private val broker = "tcp://localhost:1883"
    val scope = DeviceScope()

    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {


        C.promptForExit()
        C.box("Starting Up Main Program")
        Bus.deviceListener = PiDeviceListener()
        MQTT.broker = broker






            Robot.update(SerialPort)
            Robot.update(MQTT)
            Robot.update(Sonar)
            Robot.update(Motor)

            Robot.start()



        while(true) {
            Thread.sleep(10)
        }




   }


}


