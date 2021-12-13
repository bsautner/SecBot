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


    var screenHeight: Float = 0.0F
    var screenWidth:  Float = 0.0F

    var compass by mutableStateOf(0.0F)
    var lidardata by mutableStateOf(Lidar)





}