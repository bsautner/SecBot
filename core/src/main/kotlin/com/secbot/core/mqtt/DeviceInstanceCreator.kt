package com.secbot.core.mqtt

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.secbot.core.hardware.Compass
import com.secbot.core.hardware.Device
import com.secbot.core.hardware.DeviceType
import com.secbot.core.hardware.Lidar
import java.lang.reflect.Type

class DeviceInstanceCreator : JsonDeserializer<Device> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Device {


        return when (DeviceType.valueOf(json.asJsonObject["device"].asString)) {

            DeviceType.COMPASS -> {
                 GsonBuilder().create().fromJson(json, Compass::class.java )

            }
            DeviceType.LIDAR-> {
                 GsonBuilder().create().fromJson(json, Lidar::class.java )

            }
            else -> throw RuntimeException("woops")
        }

    }


}