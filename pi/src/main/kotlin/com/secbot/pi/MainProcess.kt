package com.secbot.pi

import com.hopding.jrpicam.RPiCamera
import com.secbot.core.SecBot
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.io.SerialPort
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val secBot: SecBot, private val serialComm: SerialPort, private val mqtt: MQTT, private val camera : Optional<RPiCamera>)   {

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
       //     mqtt.start().also {
            serialComm.start()

        //    }

        }

        var control = 1000
        while (serialComm.isConnected()) {
            delay(10)
            control -= 10
            if (control < 0) {
                serialComm.send("ping")
                control = 1000
                       }

        }
        println("exiting...")
     }






}


