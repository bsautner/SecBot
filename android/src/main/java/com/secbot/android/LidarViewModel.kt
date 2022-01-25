package com.secbot.android

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.devices.lidar.LidarPoint

class LidarViewModel : ViewModel() {

    var center : Offset = Offset(0F, 0F)
    var compass: Float = 0.0F
    var screenHeight: Float = 0.0F
    var screenWidth: Float = 0.0F

    val live : MutableLiveData<String> = MutableLiveData("")
    val lidar : MutableLiveData<Lidar> = MutableLiveData(Lidar())
    val payload : MutableLiveData<JsonArray> = MutableLiveData()
    val touch: MutableLiveData<Offset> = MutableLiveData()





}