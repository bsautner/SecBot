package com.secbot.core

import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

object Bus {

    lateinit var deviceListener: DeviceListener
    val scope = DeviceScope()

    fun post(payload: String) {
       scope.launch {
            println("BEN: Bus sending payload on ${Thread.currentThread().name} $payload ${Bus::deviceListener.isInitialized}")
            deviceListener.onReceive(payload)
        }

    }
}