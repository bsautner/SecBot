package com.secbot

import com.hopding.jrpicam.RPiCamera
import com.hopding.jrpicam.enums.Exposure
import com.pi4j.util.Console
import com.secbot.api.DefaultApiClient
import com.secbot.serial.ArduinoSerialInterface
import com.secbot.serial.SerialPortListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*

class Application : SerialPortListener {


    enum class State {
        IDLE, STOPPED, BACKINGUP, CLEAR, LOOKING
    }

    private val arduinoSerialInterface = ArduinoSerialInterface(this)
    private lateinit var piCamera: RPiCamera
    private val apiClient = DefaultApiClient()

    private var forwardSpaceCm = -1
    private var state = State.IDLE

    override fun onRead(rawData: String) {


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
        return File("$PHOTO_PATH/$file")


    }



    fun initHardware() {

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


//        arduinoSerialInterface.sendCommand("S1:45")
//        arduinoSerialInterface.sendCommand("M1:0")


    }

    @Throws(IOException::class, InterruptedException::class)
    private fun start() {

        val speed = 25

        GlobalScope.launch {
           // apiClient.test()
            while (true) {
//                arduinoSerialInterface.sendCommand("S1:45")
//                break;
                println(state)
                if (forwardSpaceCm > 10 && state != State.LOOKING && state != State.BACKINGUP) {
                    state = State.CLEAR
                }

                if (state == State.CLEAR && forwardSpaceCm > 5) {
                    arduinoSerialInterface.sendCommand("M1:-$speed")
                }
                else {
                    if (forwardSpaceCm < 5) {
                        arduinoSerialInterface.sendCommand("M1:0")
                        state = State.STOPPED

                    }

                    if (state == State.STOPPED) {
                        while (forwardSpaceCm in 0..9) {
                            println("backing up")
                            state = State.BACKINGUP
                            arduinoSerialInterface.sendCommand("M1:$speed")
                        }



                    }
                    if (forwardSpaceCm >= 10 && state == State.BACKINGUP) {
                        println("looking around")
                        arduinoSerialInterface.sendCommand("M1:0")
                        state = State.LOOKING
                    }
                    if (state == State.LOOKING) {
                        println("looking for clear space ")
                        arduinoSerialInterface.sendCommand("S1:10")
                        Thread.sleep(1000)

                        arduinoSerialInterface.sendCommand("M1:$speed")
                        Thread.sleep(2000)
                        arduinoSerialInterface.sendCommand("M1:0")
                        Thread.sleep(1000)
                        arduinoSerialInterface.sendCommand("S1:70")
                        Thread.sleep(1000)
                        arduinoSerialInterface.sendCommand("M1:-$speed")
                        Thread.sleep(1000)
                        arduinoSerialInterface.sendCommand("M1:0")
                        Thread.sleep(1000)
                        arduinoSerialInterface.sendCommand("S1:45")
                        Thread.sleep(1000)
                        if (forwardSpaceCm > 10) {
                            state = State.CLEAR
                        }
                    }
                }
                Thread.sleep(1000)
            }


        }


        //arduinoSerialInterface!!.sendCommand(Command(CommandType.CALIBRATE.id, "", 0))

       // arduinoSerialInterface.sendCommand("M1:-10")


    }


    override fun onError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    companion object {
        private const val PHOTO_PATH = "/home/pi/camera"


        private var mainProgram: Application = Application()
        private var console: Console? = null
        @Throws(InterruptedException::class, IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            console = Console()

            console!!.promptForExit()
            console!!.box("Starting Up Main Program")
            mainProgram.initHardware()
            mainProgram.start()
            while (console!!.isRunning) {
                Thread.sleep(10)
                //  mainProgram.arduinoSerialInterface!!.sendCommand(Command(CommandType.PING.id, "", 0))
            }
        }
    }
}