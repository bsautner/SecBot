package com.secbot.android

import com.secbot.core.DeviceListener
import com.secbot.core.Source

class AndroidDeviceListener(private val vm: MainViewModel) : DeviceListener {
    override suspend fun onReceive(data: String) {

        println("BEN: RX $data")
        val split = data.split(',')

        when (Source.valueOf(split[0])) {
            Source.LDR -> {
               vm.lidar.update(split)
            }
            Source.MAG_SERIAL -> {}
            Source.ACC_PI -> {}
            Source.CMP -> {
               //vm.compass = split[1].toFloat()
            }
            Source.FORWARD_IR -> {
                vm.infraredRange.update(split[1].toFloat())
            }
            Source.SONAR -> {
                println("sonar: ${data}")
                vm.sonar.update(split[1].toFloat())
            }

            else -> {
                println("error: unhandled incoming data:: $data")
            }
        }
    }
}