package com.secbot.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface IO {
    fun send(command: SerialData)
    fun receive(data: SerialData)
    fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData>
    fun start(scope: CoroutineScope): ReceiveChannel<SerialData>

}