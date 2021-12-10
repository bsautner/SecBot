package com.secbot.android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.google.gson.GsonBuilder
import com.secbot.core.DeviceScope
import com.secbot.core.mqtt.MQTT
import com.secbot.core.mqtt.Payload
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    val scope: DeviceScope = DeviceScope()
    private val vm by viewModels<MainViewModel>()
    private val gson = GsonBuilder().create()

    private val feedbackListener = object : MqttCallback {

        override fun connectionLost(cause: Throwable) {
            println("MQTT Connection Lost")
            cause.printStackTrace()
        }

        override fun messageArrived(topic: String, message: MqttMessage) {


            val json = String(message.payload)
            println("MQTT Message Arrived $json")
            val payload = gson.fromJson(json, Payload::class.java)

            vm.timestamp = System.currentTimeMillis()
            vm.setValue(payload)

           // println("got mqtt data: ${gson.fromJson(json, DeviceContainer::class.java).devices.size}")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            TODO("Not yet implemented")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          //  mainScreen(vm)
            canvasDrawExample(vm)
        }

    }

    override fun onResume() {
        super.onResume()
        println("BEN::channel opening")
        scope.async {
            MQTT.start(AndroidDeviceListener(vm))
        }.start()



        println("BEN:: channel closed")
    }

    override fun onDestroy() {
        super.onDestroy()
        MQTT.stop()
    }




}