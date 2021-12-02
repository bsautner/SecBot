package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.secbot.core.Device

import com.secbot.core.mqtt.Payload
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class MainViewModel : ViewModel() {


    var compass by mutableStateOf(0.0F)
    var lidardata by mutableStateOf(mutableMapOf<Int, Int>())
    var timestamp by mutableStateOf(0L)

    fun setValue(payload: Payload) {

        timestamp = System.currentTimeMillis()

        val split = payload.data.split(',')
        when (split[0]) {
            Device.MAG.name -> {
                compass = split[1].toFloat()
            }
            Device.LDR.name -> {
                lidardata[split[1].toInt()] = split[2].toInt()
            }
        }



    }


    fun sendCommand() {
        GlobalScope.launch {
           // mqtt.publishDeviceCommand(DeviceCommand(Control.SCANNING_SERVO, 30.0))
        }

    }
}