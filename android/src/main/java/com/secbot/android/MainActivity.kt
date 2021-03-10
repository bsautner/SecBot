package com.secbot.android

import com.secbot.common.App
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.secbot.core.SerialData
import com.secbot.core.mqtt.FeedbackListener
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {


    private val mqtt: MQTT = MQTT()
    private val feedbackListener = object : FeedbackListener {
        override fun onRevieve(data: SerialData) {
            println("listener mqqt data : ${data.toSerialCommand()}")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        mqtt.feedbackListener = this.feedbackListener
    }

    override fun onResume() {
        super.onResume()
        //incomming MQTT DATA
        MainScope().launch {

            val s = mqtt.start(this)
            val r = mqtt.receiver(this, s)

            delay(1000)
            while (! r.isClosedForReceive) {
                val data: SerialData = r.receive()
                println("Main Process got mqtt data: ${data.toSerialCommand()} on thread ${Thread.currentThread().name}")
                // mqtt.sendCommand(data)
            }

            println("channel closed")

        }

     }


}