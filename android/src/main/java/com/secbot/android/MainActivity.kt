package com.secbot.android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.google.gson.GsonBuilder
import com.secbot.core.data.DeviceCommand
import com.secbot.core.data.SensorData
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()
    private val mqtt = MQTT()
    private val gson = GsonBuilder().create()
    private val feedbackListener = object : MqttCallback {

        override fun connectionLost(cause: Throwable) {
            TODO("Not yet implemented")
        }

        override fun messageArrived(topic: String, message: MqttMessage) {

            val json = String(message.payload)
            println("got mqtt data: $json")

            vm.setValue(gson.fromJson(json, SensorData::class.java))
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            TODO("Not yet implemented")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mqtt.subscribeDeviceCommands()
        setContent {
            mainScreen(vm)
        }

    }

    override fun onResume() {
        super.onResume()


        println("channel closed")
        GlobalScope.launch {
                mqtt.start()
                mqtt.subscribeSensorData(feedbackListener)

               // delay(1000)
//                while (! r.isClosedForReceive) {
//                    val data: SensorData = r.receive()
//                 //   println("Main Process got mqtt data: ${data.toSerialCommand()} on thread ${Thread.currentThread().name}")
//                    // mqtt.sendCommand(data)
//                }
                //println("mqtt closed")


        }
    }


}