package com.secbot.pi.devices.serial

import com.pi4j.io.serial.*
import com.secbot.core.AbstractDevice
import com.secbot.core.Bus
import com.secbot.core.Source
import com.secbot.pi.devices.C
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


object SerialPort : AbstractDevice<String>() {

    private const val ttyUSB0 = "ttyUSB0"
    private const val ttyUSB1 = "ttyUSB1"

    private val serial: Serial = SerialFactory.createInstance()

    override fun start() {
        super.start()
        scope.launch {
            read()
        }




    }

    private suspend fun read() {
        val tty0 = File("/dev/$ttyUSB0")
        val tty1 = File("/dev/$ttyUSB1")
        println("finding serial $tty0 = ${tty0.exists()}  $tty1 = ${tty1.exists()}")

        val tty : String = if (tty0.exists()) {
            ttyUSB0
        } else if (tty1.exists()) {
            ttyUSB1
        } else {
            throw RuntimeException("Can't find a serial port :(")
        }

            println("starting serial $tty on thread: ${Thread.currentThread().name}")
            val config = SerialConfig()
            config.device("/dev/$tty")
                .baud(Baud._115200)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE)



            serial.open(config)
            delay(100)
            serial.flush()
            serial.discardAll()

            println("Started Serial Port ${serial.isOpen}")
        update(Source.PING.name)


        serial.addListener(SerialDataEventListener {

            if (it.asciiString.isNotEmpty()) {
               // println("serial data:: ${it.asciiString}")
                val split = it.asciiString.trim().split('\n')
                split.forEach { part ->

                    scope.async {
                        println("serial RX $part")

                       Bus.post(part.trim())
                    }.start()

                }
            }
        })
    }


    override fun update(payload: String) {

        if (serial.isOpen) {
            println("Serial TX $payload")

            serial.writeln(payload)

        } else {
            C.print("Can't send serial command because port is closed")
        }


    }



}


