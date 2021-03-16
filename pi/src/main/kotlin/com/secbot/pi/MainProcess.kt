package com.secbot.pi

import com.hopding.jrpicam.RPiCamera
import com.secbot.core.SecBot
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.io.SerialManager
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val secBot: SecBot, private val sensorSerialPort: SerialManager, private val commandPort: SerialManager, private val mqtt: MQTT, private val camera : Optional<RPiCamera>)   {

    // private val apiClient = DefaultApiClient()


    private fun takePhoto() : Optional<File> {



        if (camera.isPresent) {
            val file = "${UUID.randomUUID()}.jpg"
            camera.get().takeStill(file, 500, 500)
            println("Took a photo of obstruction $file")
           return Optional.of(File("${PHOTO_PATH}/$file"))
        }
        return Optional.empty()


    }



    @Throws(IOException::class, InterruptedException::class)
    suspend fun start() {

        var t = System.currentTimeMillis()

        GlobalScope.runCatching {
            mqtt.start().also {
                sensorSerialPort.start()
              //  commandPort.start()
            }

        }

        while (sensorSerialPort.isConnected()) {
            delay(10)
            if (System.currentTimeMillis() - t > 1000) {
                t = System.currentTimeMillis()
              //  commandPort.sendCommand(DeviceCommand(Control.PING, t.toDouble()))

            }
        }
     }






}


