package com.secbot.core

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.secbot.core.hardware.Compass
import com.secbot.core.hardware.Device
import com.secbot.core.hardware.Lidar
import com.secbot.core.hardware.DeviceType

class SensorDataProcessor(private val gson: Gson) {

    fun process(json: String): Device {

        val jsonObject: JsonObject = JsonParser.parseString(json).asJsonObject
        return when (DeviceType.valueOf(jsonObject["device"].asString)) {
            DeviceType.COMPASS -> {
                gson.fromJson(json, Compass::class.java)


            }
            DeviceType.LIDAR -> {
                gson.fromJson(json, Lidar::class.java)
            }
            DeviceType.FRONT_SONAR -> TODO()

        }

    }
}