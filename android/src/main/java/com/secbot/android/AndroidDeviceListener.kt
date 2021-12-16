package com.secbot.android

import com.secbot.core.DeviceListener
import com.secbot.core.Source

class AndroidDeviceListener(private val vm: MainViewModel) : DeviceListener {
    override suspend fun onReceive(data: String) {

        val split = data.split(',')

        when (Source.valueOf(split[0])) {
            Source.LDR -> {
               vm.lidar.update(split, false)
            }
            Source.MAG_SERIAL -> {}
            Source.ACC_PI -> {}
            Source.CMP -> {
               vm.compass = split[1].toFloat()
            }
        }
    }
}