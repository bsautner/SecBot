package com.secbot.android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.secbot.core.SerialData
import com.secbot.core.mqtt.FeedbackListener
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()
    private val mqtt = MQTT()
    private val feedbackListener = object : FeedbackListener {
        override fun onRevieve(data: SerialData) {
            println("listener mqqt data : ${data.toSerialCommand()}")
            vm.setValue(data.toSerialCommand())

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mqtt.feedbackListener = feedbackListener
        setContent {
            mainScreen(vm)
        }

    }

    override fun onResume() {
        super.onResume()


        println("channel closed")
        GlobalScope.launch {
                 val s = mqtt.start(this)
                val r = mqtt.receiver(this, s)

                delay(1000)
                while (! r.isClosedForReceive) {
                    val data: SerialData = r.receive()
                    println("Main Process got mqtt data: ${data.toSerialCommand()} on thread ${Thread.currentThread().name}")
                    // mqtt.sendCommand(data)
                }
                println("mqtt closed")


        }
    }


}