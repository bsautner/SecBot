package com.secbot.core.hardware

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DeviceContainer {

    @SerializedName("devices")
    val devices : MutableList<Device> = ArrayList()


    fun put(device: Device) {
        devices.add(device)

    }

    fun isNotEmpty(): Boolean {
        return devices.isNotEmpty()
    }

}