package com.secbot.core.mqtt

import com.secbot.core.SerialData

interface FeedbackListener {


    fun onRevieve(data : SerialData)
}