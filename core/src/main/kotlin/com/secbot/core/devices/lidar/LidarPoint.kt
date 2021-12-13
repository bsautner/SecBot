package com.secbot.core.devices.lidar

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

data class LidarPoint(val distance : AtomicInteger, val timestamp: AtomicLong = AtomicLong(System.currentTimeMillis()))
