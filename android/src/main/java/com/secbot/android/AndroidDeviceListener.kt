package com.secbot.android

import com.secbot.core.DeviceListener
import com.secbot.core.Source

class AndroidDeviceListener(private val vm: MainViewModel) : DeviceListener {
    override suspend fun onReceive(data: String) {

        val split = data.split(',')
        val source = Source.valueOf(split[0])

        when (source) {
            Source.LDR -> {}
            Source.MAG -> {}
            Source.ACC -> {}
            Source.CMP -> {
               vm.compass = split[1].toFloat()
            }
        }
    }
}