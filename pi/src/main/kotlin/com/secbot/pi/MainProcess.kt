package com.secbot.pi

import com.google.gson.GsonBuilder
import com.hopding.jrpicam.RPiCamera
import com.secbot.core.Device
import com.secbot.core.mqtt.MQTT
import com.secbot.core.mqtt.Payload
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.pi.io.SerialListener
import com.secbot.pi.io.SerialPort
import com.secbot.pi.lidar.LidarBuffer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val serialComm: SerialPort, private val mqtt: MQTT, private val camera : Optional<RPiCamera>)   {


    private val serialListener =  object : SerialListener {
        override fun onReceive(data: String) {

            val split = data.split(',')
            when (split[0]) {
                Device.LDR.name -> {
                    val distance = BigDecimal.valueOf(split[1].toDouble()).toInt()
                    val angle = BigDecimal.valueOf(split[2].toDouble()).toInt()
                    val buffer = LidarBuffer.update(angle, distance)
                    if (buffer.isNotEmpty()) {
                        println("ready to transmit lidar changes: ${buffer.size}")
                        buffer.forEach {
                            val a = it.key
                            val d = it.value
                            val s = "${Device.LDR.name},$a,$d"


                            mqtt.publish(s)
                        }


                    }
                }
                Device.MAG.name -> {

                }

            }
        }


    }


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

        serialComm.listener = serialListener

        GlobalScope.runCatching {
      //  Runtime.getRuntime().exec("mpg123 /home/pi/speech/online.mp3")



            val file = takePhoto()
            println("Camera Working: ${file.isPresent}")
            if (file.isPresent) {
                println("startup photo: ${file.get().absolutePath}")
            }
            mqtt.start().also {
            serialComm.start()

            }

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


