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



    private val arduinoSerialInterface = ArduinoSerialInterface(this)
    private lateinit var piCamera: RPiCamera
    private val apiClient = DefaultApiClient()

    override fun onRead(rawData: String) {
        println("read: $rawData")


        if (rawData.isNotEmpty()) {
            val sanitized = rawData.trimEnd('\n').trimEnd('\r').trimEnd('\n')
            when (sanitized) {
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
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun start() {

        GlobalScope.launch {
            apiClient.test()
        }


        //arduinoSerialInterface!!.sendCommand(Command(CommandType.CALIBRATE.id, "", 0))
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