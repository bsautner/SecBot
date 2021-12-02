package com.secbot.pi.io

interface SerialListener {

    fun onReceive(data: String)
}