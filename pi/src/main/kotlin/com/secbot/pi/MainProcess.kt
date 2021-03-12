package com.secbot.pi

import com.hopding.jrpicam.RPiCamera
import com.secbot.core.SecBot
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.core.data.SensorData
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.io.SensorSerialPortManager
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val secBot: SecBot, private val sensorSerialPort: SensorSerialPortManager, private val mqtt: MQTT, private val camera : Optional<RPiCamera>)   {

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

        GlobalScope.launch {
            sensorSerialPort.start()

            while (true) {
                delay(10)
            }


        }
    }






}


