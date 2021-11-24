package com.secbot.pi.io

import com.google.gson.Gson
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.serial.*
import com.secbot.core.SensorDataProcessor
import com.secbot.core.hardware.*
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*
import java.lang.NullPointerException


@ExperimentalCoroutinesApi
class SerialPort  (
    private val processor: SensorDataProcessor,
    private val serial: Serial,
    private val mqtt: MQTT,
    private val tty: String,
    private val gson: Gson) {


    suspend fun start() {
        val gpio: GpioController = GpioFactory.getInstance()

        val ledPin: GpioPinDigitalOutput = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25)

        ledPin.low()

        println("starting serial $tty on thread: ${Thread.currentThread().name}")
        val config = SerialConfig()
        config.device("/dev/$tty")
            .baud(Baud._115200)
            .dataBits(DataBits._8)
            .parity(Parity.NONE)
            .stopBits(StopBits._1)
            .flowControl(FlowControl.NONE)
        serial.open(config)
        serial.flush()
        delay(100)
        println("Started Feedback Port")



        serial.addListener(SerialDataEventListener {

            if (it.asciiString.isNotEmpty()) {

                println(it.asciiString.trim())

                when(it.asciiString.trim()) {
                    "pong" -> {
                        println("blink")
                        GlobalScope.launch {
                            ledPin.high()
                            delay(1000)
                            ledPin.low()
                        }


                    }
                }
               // println("blink")

               // ledPin.blink(100, 2000)





            }
        })
    }

    fun send(command: String) {

        if (serial.isOpen) {
            kotlin.runCatching {    serial.writeln( command) }

        } else {
            println("Can't send serial command because port is closed")
        }
    }

    fun isConnected(): Boolean {
        return serial.isOpen
    }



}


