package com.secbot.core.hardware

 data class Compass  (

     val device: String = DeviceType.COMPASS.name,
     val x: Double = 0.0,
     val y: Double = 0.0,
     val z: Double = 0.0,
     val heading: Double = 0.0) : Device {

     override fun deviceType(): DeviceType {
        return DeviceType.valueOf(device)
     }


 }


