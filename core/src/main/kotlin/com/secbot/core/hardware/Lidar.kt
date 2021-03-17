package com.secbot.core.hardware

data class Lidar (
    val device: String = DeviceType.LIDAR.name,
    val distance: Double,
    val angle: Double,
    val quality: Double = 0.0,
    val startBit: Boolean = true) : Device {

    override fun deviceType(): DeviceType {
        return DeviceType.valueOf(device)
    }
    }