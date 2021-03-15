package com.secbot.pi.io

import com.secbot.core.data.DeviceCommand

interface SerialManager {

    suspend fun start()
    suspend fun sendCommand(command: DeviceCommand)
    fun isConnected() : Boolean

}