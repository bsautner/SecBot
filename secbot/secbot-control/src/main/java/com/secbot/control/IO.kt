package com.secbot.control

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface IO {
    fun sendCommand(command: SerialData)
    fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData>
    fun start(scope: CoroutineScope): ReceiveChannel<SerialData>

}