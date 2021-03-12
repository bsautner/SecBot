package com.secbot.core.data

import com.secbot.core.hardware.Sensor

data class SensorData(private val device: String, val reading: Double, var timestamp: Long = System.currentTimeMillis()) {

    constructor( device: Sensor,  reading: Double) : this(device.name, reading, System.currentTimeMillis())

    val sensor : Sensor
        get() {return Sensor.valueOf(device)}



}


