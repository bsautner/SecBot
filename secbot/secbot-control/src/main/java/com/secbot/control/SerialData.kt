package com.secbot.control

data class SerialData(val device: Device, private var v: Double, var timestamp: Long) {

    var value: Double = v
        set(value) {
            v = value
            timestamp = System.currentTimeMillis()

            field = value
        }

    fun toSerialCommand() : String {
        return "${device.name}:$v:$timestamp\n"
    }

    override fun toString(): String {
        return "SerialData(device=$device, v=$v, timestamp=$timestamp, value=$value)"
    }


}


