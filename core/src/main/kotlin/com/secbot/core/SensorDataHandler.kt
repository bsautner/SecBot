package com.secbot.core

import com.secbot.core.data.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface SensorDataHandler {
    fun receiver(scope: CoroutineScope, data: ReceiveChannel<SensorData>): ReceiveChannel<SensorData>
    fun start(scope: CoroutineScope): ReceiveChannel<SensorData>
}