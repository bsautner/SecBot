package com.secbot.core.hardware

 open class Device(private val deviceType: String, private val timestamp: Long = System.currentTimeMillis())  {


     fun deviceType() : DeviceType {
         return DeviceType.valueOf(deviceType)
     }
}

