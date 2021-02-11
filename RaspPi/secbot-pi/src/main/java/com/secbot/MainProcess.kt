package com.secbot

import com.hopding.jrpicam.RPiCamera
import com.hopding.jrpicam.enums.Exposure
import com.secbot.api.DefaultApiClient
import com.secbot.serial.SerialPortIO
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val serialPort: SerialPortIO)   {

    private lateinit var piCamera: RPiCamera
    private val apiClient = DefaultApiClient()
   // private val sim = WorldSimulator(this)

    private var forwardSpaceCm = -1
    private var state = State.IDLE


      fun onRead(rawData: String) {


        if (rawData.isNotEmpty()) {
            val sanitized = rawData.trimEnd('\n').trimEnd('\r').trimEnd('\n')
        //    println("Serial Data: $sanitized")
            val data = sanitized.split(":")
            when (data[0]) {

                "front_sonar" -> {
                    forwardSpaceCm = data[1].toInt()

                }


                "Obstical Detected" -> {

                    GlobalScope.async {
                        println("Taking photo")
                        val file = takePhoto()
                        apiClient.uploadPhoto(file)
                    }

                }
            }
        }

    }
    private fun takePhoto() : File {


        val file = "${UUID.randomUUID()}.jpg"
        piCamera.takeStill(file, 500, 500)
        println("Took a photo of obstruction $file" )
        return File("${PHOTO_PATH}/$file")


    }




    private fun initHardware() {

            println("Initializing Hardware")

            piCamera = RPiCamera(PHOTO_PATH)
                .setWidth(300).setHeight(200)
                .setBrightness(40)
                .setExposure(Exposure.AUTO)
                .setContrast(50).setAddRawBayer(false)
                .setQuality(75)
                // .setVerticalFlipOn()
                // .setRegionOfInterest(0.5, 0.5, 0.25, 0.25)
                .setTimeout(1000)

            piCamera.setFullPreviewOn()



        GlobalScope.launch {

            println("Setting Up Serial Port")
            val s = serialPort.start(this)
            val r = serialPort.serialReceiver(this, s)
            while(true) {
                onRead(r.receive())
            }
        }
    }

    suspend fun diagSterring() {

        serialPort.sendCommand("S1:45")
        println("center")
        delay(4000)

        serialPort.sendCommand("S1:80")
        println("left")

        delay(4000)
        serialPort.sendCommand("S1:45")
        println("center")
        delay(4000)

        serialPort.sendCommand("S1:20")
         println("right")
        delay(4000)


//        for ( i in 20..70) {
//            serialPort.sendCommand("S1:$i")
//            println("Diagnosing Steering right $i")
//            delay(500)
//        }
//
//        for ( i in 70 downTo 20) {
//            serialPort.sendCommand("S1:$i")
//            println("Diagnosing Steering left $i")
//            delay(500)
//        }
    }


    @Throws(IOException::class, InterruptedException::class)
    suspend fun start() {
        initHardware()
        var speed: Int

        while (true) {
            diagSterring()
        }


        while (false) {
                speed = when  {
                    state == State.BACKING_UP -> {
                        15
                    }
                    forwardSpaceCm > 500 -> {
                        50
                    }
                    forwardSpaceCm in 11..99 -> {
                        15
                    }
                    forwardSpaceCm < 10 &&  state != State.BACKING_UP -> {
                        0
                    }

                    else -> {
                        25
                    }
                }

//                println(state)
                if (forwardSpaceCm > 10 && state != State.LOOKING && state != State.BACKING_UP) {
                    state = State.CLEAR
                }

                if (state == State.CLEAR && forwardSpaceCm > 5) {
                    serialPort.sendCommand("M1:-$speed")
                }
                else {
                    if (forwardSpaceCm < 5) {
                        serialPort.sendCommand("M1:0")
                        state = State.STOPPED

                    }

                    if (state == State.STOPPED) {
                        speed = 20
                        while (forwardSpaceCm in 0..9) {
                            println("backing up $speed $forwardSpaceCm")
                            state = State.BACKING_UP
                            serialPort.sendCommand("M1:$speed")
                        }



                    }
                    if (forwardSpaceCm >= 10 && state == State.BACKING_UP) {
                        println("looking around")
                        serialPort.sendCommand("M1:0")
                        state = State.LOOKING
                    }
                    if (state == State.LOOKING) {
                        println("looking for clear space ")
                        serialPort.sendCommand("S1:10")
                        delay(1000)

                        serialPort.sendCommand("M1:$speed")
                        delay(2000)
                        serialPort.sendCommand("M1:0")
                        delay(1000)
                        serialPort.sendCommand("S1:70")
                        delay(1000)
                        serialPort.sendCommand("M1:-$speed")
                        delay(1000)
                        serialPort.sendCommand("M1:0")
                        delay(1000)
                        serialPort.sendCommand("S1:45")
                        delay(1000)
                        if (forwardSpaceCm > 10) {
                            state = State.CLEAR
                        }
                    }
                }
                delay(1000)



        }
    }



    companion object {
        private const val PHOTO_PATH = "/home/pi/camera"
    }

}


