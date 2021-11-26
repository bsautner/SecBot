package com.secbot.pi

import com.hopding.jrpicam.RPiCamera
import com.pi4j.io.gpio.*
import com.secbot.core.SecBot
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.pi.io.SerialPort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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



        GlobalScope.runCatching {
        Runtime.getRuntime().exec("mpg123 /home/pi/speech/online.mp3")



            val file = takePhoto()
            println("Camera Working: ${file.isPresent}")
            if (file.isPresent) {
                println("startup photo: ${file.get().absolutePath}")
            }
            var t = System.currentTimeMillis()
       //     mqtt.start().also {
            serialComm.start()

        //    }

        }

        var control = 1000
        while (serialComm.isConnected()) {
            delay(10)
            control -= 10
            if (control < 0) {
                println("pinging")

                serialComm.send("ping")
                control = 1000
                       }

        }
        println("exiting...")
     }






}


