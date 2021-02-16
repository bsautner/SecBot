package com.secbot

import com.hopding.jrpicam.RPiCamera
import com.secbot.Const.PHOTO_PATH
import com.secbot.IO.IO
import com.secbot.api.DefaultApiClient
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
class MainProcess(private val serialPort: IO, private val camera : Optional<RPiCamera>)   {

     private val apiClient = DefaultApiClient()
   // private val sim = WorldSimulator(this)

    private var forwardSpaceCm = -1
    private var state = State.IDLE
    private var speed: Int = 0

      private fun onRead(rawData: String) {
        try {

            if (rawData.isNotEmpty()) {
                val sanitized = rawData.trimEnd('\n').trimEnd('\r').trimEnd('\n')
                println("Serial Data: $sanitized")

                val data = sanitized.split(":")
                when (data[0]) {

                    "front_sonar" -> {
                        forwardSpaceCm = data[1].toInt()

                    }


                    "Obstical Detected" -> {

                        GlobalScope.async {
                            println("Taking photo")
                            val file = takePhoto()
                            if (file.isPresent) {
                                apiClient.uploadPhoto(file.get())
                            }
                        }

                    }
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
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




    private fun initHardware() {

            println("Initializing Hardware")





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

        serialPort.sendCommand("S1:70")
        println("left")

        delay(4000)
        serialPort.sendCommand("S1:45")
        println("center")
        delay(4000)

        serialPort.sendCommand("S1:20")
         println("right")
        delay(4000)



    }


    @Throws(IOException::class, InterruptedException::class)
    suspend fun start() {
        initHardware()


//        while (true) {
//           // diagSterring()
//        }


        while (true) {
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


            when {
                (forwardSpaceCm > MAN_FRONT_SPACE_CM && state != State.LOOKING && state != State.BACKING_UP) -> {
                    changeState(State.CLEAR)
                    serialPort.sendCommand("M1:-$speed")
                }
                (state == State.CLEAR && forwardSpaceCm > 10) -> {
                    serialPort.sendCommand("M1:-$speed")
                }
                (forwardSpaceCm < MAN_FRONT_SPACE_CM &&  state != State.STOPPED && state != State.LOOKING && state != State.BACKING_UP) -> {
                    serialPort.sendCommand("M1:0")
                    changeState(State.STOPPED)
                }
                (state == State.STOPPED && forwardSpaceCm < MAN_FRONT_SPACE_CM) -> {
                    speed = 10
                    changeState(State.BACKING_UP)

                    serialPort.sendCommand("M1:$speed")
                }
                (forwardSpaceCm >= MAN_FRONT_SPACE_CM && state == State.BACKING_UP) -> {
                    println("looking around")
                    serialPort.sendCommand("M1:0")
                    changeState(State.LOOKING)
                }
                (state == State.LOOKING) -> {
                    println("looking for clear space ") //do some dancing
                    if (forwardSpaceCm >= MAN_FRONT_SPACE_CM ) {
                        changeState(State.CLEAR)
                    }
                }


            }
            delay(500)



        }
    }

    fun changeState(newState: State) {
        println("State Changed from $state to $newState at speed $speed with $forwardSpaceCm cm of free space")
        state = newState
    }



    companion object {

        private const val MAN_FRONT_SPACE_CM = 10
    }

}


