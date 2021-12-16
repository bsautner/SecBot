package com.secbot.core.devices.lidar

data class LidarPoint(var angle: Double = 0.0, var distance : Double = 0.0, var timestamp: Long = System.currentTimeMillis())

