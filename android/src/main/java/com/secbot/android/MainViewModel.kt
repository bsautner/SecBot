package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.secbot.core.Source
import com.secbot.core.devices.lidar.Lidar

import com.secbot.core.mqtt.Payload
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class MainViewModel : ViewModel() {


    var compass by mutableStateOf(0.0F)
    var lidardata by mutableStateOf(Lidar)
    var timestamp by mutableStateOf(0L)





}