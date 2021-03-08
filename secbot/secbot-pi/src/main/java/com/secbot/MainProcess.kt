package com.secbot

import com.hopding.jrpicam.RPiCamera
import com.secbot.control.SecBot
import com.secbot.Const.PHOTO_PATH
import com.secbot.control.IO
import com.secbot.control.SerialData
import com.secbot.mqtt.MQTT
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val secBot: SecBot, private val serialPort: IO, private val mqtt: MQTT, private val camera : Optional<RPiCamera>)   {

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



//    suspend fun diagSterring() {
//
//        serialPort.sendCommand("S1:45")
//        println("center")
//        delay(4000)
//
//        serialPort.sendCommand("S1:70")
//        println("left")
//
//        delay(4000)
//        serialPort.sendCommand("S1:45")
//        println("center")
//        delay(4000)
//
//        serialPort.sendCommand("S1:20")
//         println("right")
//        delay(4000)
//
//
//
//    }


    @Throws(IOException::class, InterruptedException::class)
    suspend fun start() {




        GlobalScope.launch {
            val s = serialPort.start(this)
            val r = serialPort.receiver(this, s)

            while(! r.isClosedForReceive) {

                val data: SerialData = r.receive()
                println("Main Process got serial data: $data on thread ${Thread.currentThread().name}")
                secBot.sendCommand(data)
                mqtt.sendCommand(data)
            }
        }

        GlobalScope.launch {
            val s = secBot.start(this)
            val r = secBot.receiver(this, s)
            while (! r.isClosedForReceive) {
                val data: SerialData = r.receive()
                println("Main Process got Pi data: ${data.toSerialCommand()} on thread ${Thread.currentThread().name}")
                serialPort.sendCommand(data)
            }

        }

        //relay serial data to MQTT Broker
        GlobalScope.launch {
            val s = mqtt.start(this)
            val r = mqtt.receiver(this, s)
            while (! r.isClosedForReceive) {
                val data: SerialData = r.receive()
                println("Main Process got mqtt data: ${data.toSerialCommand()} on thread ${Thread.currentThread().name}")
                mqtt.sendCommand(data)
            }

        }

        while (true) {
            delay(10)
        }



    }






}


