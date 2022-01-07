package com.secbot.android

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.secbot.core.DeviceScope
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.mqtt.MQTT
import com.secbot.core.mqtt.MqttListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage

@ExperimentalCoroutinesApi
class MainActivity : Activity(), MqttListener {

    private val scope: DeviceScope = DeviceScope()
   // private val vm by viewModels<MainViewModel>()
    private val broker = "tcp://10.0.0.205:1883"
    private val mqtt: MQTT = MQTT(this, broker)
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics: DisplayMetrics =  resources.displayMetrics
//        vm.screenHeight = displayMetrics.heightPixels / displayMetrics.density
//        vm.screenWidth = displayMetrics.widthPixels / displayMetrics.density
//        setContent {
//            lidarComposable(vm)
//        }

        val observer = Observer<String> {
            println(it)
        }


    }

    override fun onResume() {
        super.onResume()
        println("BEN::channel opening")
        //compass.start()
        scope.launch {
            mqtt.start()
        }





    }


    override fun onDestroy() {
        super.onDestroy()
        mqtt.stop()
    }

    override fun onConnected() {
       mqtt.subscribe(Lidar.topic)
    }

    override fun connectionLost(cause: Throwable?) {
      // Lidar.stop()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        println("MQTT RX: $topic ${message.toString()}")
        when (topic) {
            Lidar.topic -> {
                message?.let {
                 //   vm.lidar.storeChanged(gson.fromJson(String(it.payload), LidarPoint::class.java))
                  //  vm.clearance.postValue(System.currentTimeMillis())
                }


            }
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }


}