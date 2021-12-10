package com.secbot.core

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DefaultDeviceListener : DeviceListener {

    override suspend fun onReceive(data: String) {

        when (Source.valueOf(data.substringBefore(','))) {
            Source.LDR -> {}
            Source.MAG -> {}
            Source.ACC -> {}
            Source.CMP -> {
               // MQTT.publish(data)
            }
        }
    }
}