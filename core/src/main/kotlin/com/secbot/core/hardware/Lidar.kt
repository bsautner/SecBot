package com.secbot.core.hardware

data class Lidar (
    val device: String,
    val distance: Double,
    val angle: Double,
    val quality: Double,
    val startBit: Boolean
) : Device(device)