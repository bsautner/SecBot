package com.secbot.serial


import java.io.IOException

/**
 * get serial data from arduino connected to usb port and post feedback to main program
 */
class ArduinoSerialInterface(private val serialPortListener: SerialPortListener) : SerialInterface{


    val serial: SerialPortIO = SerialPortIO(serialPortListener)

    init {
        serial.start()
    }


    @Throws(IOException::class)
    override fun sendCommand(command: String) {

        serial.write(command)
        serial.write("\n")


    }
}