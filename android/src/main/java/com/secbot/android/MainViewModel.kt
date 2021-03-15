package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.hardware.Control
import com.secbot.core.hardware.Sensor
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@ExperimentalCoroutinesApi
class MainViewModel  : ViewModel() {

    val mqtt = MQTT()

    var test by mutableStateOf("FOO")

    var sensors by mutableStateOf(mutableMapOf<Sensor, SensorData>())
        private set


    fun setValue(s : SensorData) {
        test = UUID.randomUUID().toString()
        sensors[s.sensor] = s

    }


    fun sendCommand() {
        GlobalScope.launch {
            mqtt.publishDeviceCommand(DeviceCommand(Control.SCANNING_SERVO, 30.0))
        }

    }
}