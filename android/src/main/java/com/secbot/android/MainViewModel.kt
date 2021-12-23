package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.secbot.core.devices.InfraredRange
import com.secbot.core.devices.lidar.Lidar


class MainViewModel : ViewModel() {

    val maxRelevantAge = 5000

    var dragX by  mutableStateOf(0F)
    var dragY by  mutableStateOf(0F)

    var screenHeight: Float = 0.0F
    var screenWidth:  Float = 0.0F

    var compass by mutableStateOf(0.0F)
    var lidar by mutableStateOf(Lidar)
    var infraredRange by mutableStateOf(InfraredRange)

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