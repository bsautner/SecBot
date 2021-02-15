package com.secbot.IO

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface IO {
    fun sendCommand(command: String)
    fun serialReceiver(scope: CoroutineScope, data: ReceiveChannel<String>): ReceiveChannel<String>
    fun start(scope: CoroutineScope): ReceiveChannel<String>
}