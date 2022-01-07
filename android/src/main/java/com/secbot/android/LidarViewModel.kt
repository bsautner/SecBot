package com.secbot.android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.devices.lidar.LidarPoint

class LidarViewModel : ViewModel() {

    val maxRelevantAge: Int = 5000
    var compass: Float = 0.0F
    var screenHeight: Float = 0.0F
    var screenWidth: Float = 0.0F


    val live : MutableLiveData<String> = MutableLiveData("")
    val lidar : MutableLiveData<Lidar> = MutableLiveData(Lidar())
    val payload : MutableLiveData<JsonArray> = MutableLiveData()

    fun post(lidarPoint: LidarPoint?) {
        lidarPoint?.let { lidar.value?.update(it) }
        live.postValue(System.currentTimeMillis().toString())
    }



}