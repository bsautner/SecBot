package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.secbot.core.Source
import com.secbot.core.devices.lidar.Lidar

import com.secbot.core.mqtt.Payload
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class MainViewModel : ViewModel() {

    val maxRelevantAge = 5000

    var screenHeight: Float = 0.0F
    var screenWidth:  Float = 0.0F

    var compass by mutableStateOf(0.0F)
    var lidar by mutableStateOf(Lidar)

    fun getClosestObstacle() : Double {
        var result = Double.MAX_VALUE

        lidar.data.filterValues {
            (it.distance > 0.0) and (System.currentTimeMillis() - it.timestamp < maxRelevantAge)
        }.forEach {
            if (it.value.distance < result) { result = it.value.distance }
        }


        return result
    }


}