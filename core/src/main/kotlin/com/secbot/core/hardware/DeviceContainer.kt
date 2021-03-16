package com.secbot.core.hardware

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.HashMap

class DeviceContainer {

    @SerializedName("devices")
    val devices : MutableMap<DeviceType, Device> = EnumMap(DeviceType::class.java)


    fun put(device: Device) {
        devices[device.deviceType()] = device

    }

    fun isNotEmpty(): Boolean {
        return devices.isNotEmpty()
    }

}