package com.secbot.pi.io

import com.secbot.core.hardware.Device


interface SerialManager {

    suspend fun start()
    suspend fun sendCommand(device: Device)
    fun isConnected() : Boolean

}