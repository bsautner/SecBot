@file:OptIn(ExperimentalComposeUiApi::class)

package com.secbot.android

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.secbot.core.DeviceScope
import com.secbot.core.Motion
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.devices.motor.MotorCommand
import com.secbot.core.mqtt.MQTT
import com.secbot.core.mqtt.MqttListener
import com.secbot.core.mqtt.Topic
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage

class LidarActivity : ComponentActivity(), MqttListener {

    private val scope: DeviceScope = DeviceScope()
    // private val vm by viewModels<MainViewModel>()
    private val broker = "tcp://10.0.0.205:1883"
    private val mqtt: MQTT = MQTT(this, broker)
    private val gson = Gson()
    private val vm by viewModels<LidarViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayMetrics: DisplayMetrics =  resources.displayMetrics
        vm.screenHeight = displayMetrics.heightPixels / displayMetrics.density
        vm.screenWidth = displayMetrics.widthPixels / displayMetrics.density
        setContent {
            lidarComposable(vm)
        }

        vm.payload.observe(this) {}

        vm.touch.observe(this) {
          sendMotorCommand(it)


        }

    }

    private fun sendMotorCommand(offset: Offset) {
        val motion : Motion = if (offset == vm.center) {
            Motion.STOP
        } else if (offset.y > vm.center.y) {
            Motion.REVERSE
        } else {
            Motion.FORWARD
        }


        val steering: Motion = if (offset.x < vm.center.x - 10.0F) {
            Motion.LEFT
        } else if (offset.x > vm.center.x + 10.0F) {
            Motion.RIGHT
        } else {
            Motion.STOP
        }

        mqtt.publish(MotorCommand::class.java.simpleName, gson.toJson(MotorCommand(motion)))
        mqtt.publish(MotorCommand::class.java.simpleName, gson.toJson(MotorCommand(steering)))

    }


    override fun onResume() {
        super.onResume()
        scope.launch {
            kotlin.runCatching {
                mqtt.start()
            }

        }
    }

    override fun onConnected() {
        Topic.values().forEach {
            mqtt.subscribe(it.name)
        }


    }

    override fun connectionLost(cause: Throwable?) {
        println("MQTT Connection Lost")
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        when (Topic.valueOf(topic)) {
            Topic.RAW_LIDAR -> {
                val payload = gson.fromJson(String(message.payload), JsonArray::class.java)
                vm.payload.postValue(payload)
            }
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) { }
}


@Composable
fun lidarScreen(vm: LidarViewModel) {

    val lidarObserver : Lidar by vm.lidar.observeAsState(vm.lidar.value ?: Lidar())
    val t : String by vm.live.observeAsState("")


    MaterialTheme {
        Column {
            Row {
                Text(text = "Hello $t")
            }
            Row {
                Canvas(modifier = Modifier.fillMaxSize()) {

                    lidarObserver.data.forEach {
                        drawCircle(Color.Blue, 10F, Offset(it.value.angle.toFloat(), it.value.distance.toFloat()))
                    }
                    drawCircle(Color.Black, 20F, this.center)

                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    lidarScreen(LidarViewModel())
}