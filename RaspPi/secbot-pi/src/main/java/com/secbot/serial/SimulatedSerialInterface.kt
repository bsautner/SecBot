package com.secbot.serial

class SimulatedSerialInterface(private val serialPortListener: SerialPortListener) : SerialInterface {
    override fun sendCommand(command: String) {
        println("Sending Simulated Command over serial: $command")
    }
}