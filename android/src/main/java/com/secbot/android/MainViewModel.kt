package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.secbot.core.SensorDataProcessor
import com.secbot.core.hardware.Compass
import com.secbot.core.hardware.Device
import com.secbot.core.hardware.DeviceContainer

import com.secbot.core.hardware.DeviceType
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {


    var test by mutableStateOf("FOO")
    var compass by mutableStateOf(Compass())

    var sensors by mutableStateOf(mutableMapOf<DeviceType, Device>( ))



    fun setValue(deviceContainer: DeviceContainer) {
        test = UUID.randomUUID().toString()
        deviceContainer.devices.forEach {
            when (it.key) {

                DeviceType.COMPASS -> {
                    compass = it.value as Compass
                }
                DeviceType.LIDAR -> {

                }
            }
        }

    }


    fun sendCommand() {
        GlobalScope.launch {
           // mqtt.publishDeviceCommand(DeviceCommand(Control.SCANNING_SERVO, 30.0))
        }

    }
}