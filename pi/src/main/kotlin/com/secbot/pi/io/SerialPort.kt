package com.secbot.pi.io

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.serial.*
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class SerialPort(private val serial: Serial, private val tty: String) {

    lateinit var listener: SerialListener



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
                val split = it.asciiString.trim().split('\n')
                split.forEach { part ->
                    //println("serial data:: $part")

                    when(part.trim()) {
                        "pong" -> {
                            GlobalScope.launch {
                                ledPin.high()
                                delay(100)
                                ledPin.low()
                            }


                        }
                        else -> {
                            listener.onReceive(part)
                        }
                    }
                }








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


