package com.secbot.pi.io

import com.pi4j.io.serial.*
import kotlinx.coroutines.delay

abstract class SerialPort(private val serial: Serial, private val tty: String) : SerialManager {

    override fun isConnected(): Boolean {
        return serial.isOpen
    }

    override suspend fun start() {
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
    }
}