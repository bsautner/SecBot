package com.secbot.control

data class SerialData(val device: Device, private var v: Double, private var timestamp: Long) {

    var value: Double = v
        set(value) {
            v = value
            timestamp = System.currentTimeMillis()

            field = value
        }

    fun toSerialCommand() : String {
        return "${device.name}:$v\n"
    }

    override fun toString(): String {
        return "SerialData(device=$device, v=$v, timestamp=$timestamp, value=$value)"
    }


}


