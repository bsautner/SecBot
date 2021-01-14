package com.secbot.serial


import java.io.IOException

/**
 * get serial data from arduino connected to usb port and post feedback to main program
 */
class ArduinoSerialInterface(listener: SerialPortListener) {

    val serial: SerialPortIO = SerialPortIO(listener)

    init {
        serial.start()
    }


    @Throws(IOException::class)
    fun sendCommand(command: String) {

        serial.write(command)
        serial.write("\n")


    }
}