package com.secbot.pi.devices.serial

import com.pi4j.io.serial.*
import com.secbot.core.AbstractDevice
import com.secbot.core.DeviceListener
import com.secbot.pi.devices.C
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object SerialPort : AbstractDevice() {

    private const val tty = "ttyUSB0"

    private val serial: Serial = SerialFactory.createInstance()

    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)
        scope.async {
            read()
            while (stopped.not()) {

                delay(100)
            }
        }.start()
    }

    private suspend fun read() {


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
            println("Started Serial Port")



        serial.addListener(SerialDataEventListener {

            if (it.asciiString.isNotEmpty()) {
               // println("serial data:: ${it.asciiString}")
                val split = it.asciiString.trim().split('\n')
                split.forEach { part ->

                    scope.async {
                      //  println("serial data:: $part")
                       deviceListener.onReceive(part.trim())
                    }.start()

                }
            }
        })
    }


    fun send(command: String) {

        if (serial.isOpen) {
            scope.runCatching { serial.writeln(command) }

        } else {
       //     C.print("Can't send serial command because port is closed")
        }
    }

    fun isConnected(): Boolean {
        return serial.isOpen
    }


}


