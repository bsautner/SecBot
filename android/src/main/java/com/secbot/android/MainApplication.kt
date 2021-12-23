package com.secbot.android

import android.app.Application
import com.secbot.core.Bus
import com.secbot.core.devices.InfraredRange
import com.secbot.core.mqtt.MQTT

class MainApplication : Application() {

    val deviceListener = AndroidDeviceListener(MainViewModel())

    override fun onCreate() {
        super.onCreate()
        Bus.deviceListener = deviceListener


    }
}