package com.secbot.pi.devices.serial

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.serial.*
import com.secbot.core.AbstractDevice
import com.secbot.core.DeviceListener
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
object SerialPort : AbstractDevice() {

    private const val tty = "ttyUSB0"

    private val serial: Serial = SerialFactory.createInstance()

    override suspend fun start(deviceListener: DeviceListener) {
        scope.launch {
            read()
            while (stopped.not()) {

                delay(100)
            }
        }
    }
    private suspend fun read() {
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
                            scope.launch {
                                ledPin.high()
                                delay(100)
                                ledPin.low()
                            }


                        }
                        else -> {

                            postPart(part)
                        }
                    }
                }








            }
        })
    }

    private fun postPart(part: String) {
        scope.launch {
            deviceListener.onReceive(part)
        }

    }
    fun send(command: String) {

        if (serial.isOpen) {
            scope.runCatching {    serial.writeln( command) }

        } else {
           // println("Can't send serial command because port is closed")
        }
    }

    fun isConnected(): Boolean {
        return serial.isOpen
    }




}


