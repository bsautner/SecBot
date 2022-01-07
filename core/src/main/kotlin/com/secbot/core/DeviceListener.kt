package com.secbot.core

interface DeviceListener {

   suspend fun onReceive(topic: String, data: String)
}