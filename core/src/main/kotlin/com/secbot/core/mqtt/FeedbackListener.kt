package com.secbot.core.mqtt

import com.secbot.core.data.SensorData

interface FeedbackListener {


    fun onRevieve(data : SensorData)
}