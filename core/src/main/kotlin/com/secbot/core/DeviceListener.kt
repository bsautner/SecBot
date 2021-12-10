package com.secbot.core

interface DeviceListener {

   suspend fun onReceive(data: String)
}