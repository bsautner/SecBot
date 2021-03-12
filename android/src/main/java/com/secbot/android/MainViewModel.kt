package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.secbot.core.data.SensorData
import com.secbot.core.hardware.Sensor
import java.util.*


class MainViewModel  : ViewModel() {

    var test by mutableStateOf("FOO")

    var sensors by mutableStateOf(mutableMapOf<Sensor, SensorData>())
        private set


    fun setValue(s : SensorData) {
        test = UUID.randomUUID().toString()
        sensors[s.sensor] = s

    }
}