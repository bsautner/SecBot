package com.secbot.android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.google.gson.GsonBuilder
import com.secbot.core.hardware.Device
import com.secbot.core.hardware.DeviceContainer
import com.secbot.core.mqtt.DeviceInstanceCreator
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()
    private val mqtt = MQTT()
    private val gson = GsonBuilder().registerTypeAdapter(Device::class.java, DeviceInstanceCreator()).create()
    private val feedbackListener = object : MqttCallback {

        override fun connectionLost(cause: Throwable) {
            println("MQTT Connection Lost")
            cause.printStackTrace()
        }

        override fun messageArrived(topic: String, message: MqttMessage) {


            val json = String(message.payload)
         //   println("Message Arrived $json")
            val deviceContainer = gson.fromJson(json, DeviceContainer::class.java)

            vm.setValue(deviceContainer)
           // println("got mqtt data: ${gson.fromJson(json, DeviceContainer::class.java).devices.size}")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            TODO("Not yet implemented")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mqtt.subscribeDeviceCommands()
        setContent {
          //  mainScreen(vm)
            canvasDrawExample(vm)
        }

    }

    override fun onResume() {
        super.onResume()



        GlobalScope.launch {
               MqttService(mqtt, feedbackListener).monitor()


        }
        println("channel closed")
    }


}