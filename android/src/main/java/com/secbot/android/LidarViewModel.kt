package com.secbot.android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.secbot.core.DeviceScope
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.devices.lidar.LidarPoint
import com.secbot.core.mqtt.MQTT
import com.secbot.core.mqtt.MqttListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage

class LidarViewModel : ViewModel(), MqttListener {

    val maxRelevantAge: Int = 5000
    var compass: Float = 0.0F
    var screenHeight: Float = 0.0F
    var screenWidth: Float = 0.0F
    private val scope: DeviceScope = DeviceScope()
    // private val vm by viewModels<MainViewModel>()
    private val broker = "tcp://10.0.0.205:1883"
    private val mqtt: MQTT = MQTT(this, broker)
    private val gson = Gson()

    val live : MutableLiveData<String> = MutableLiveData("")
    val lidar : MutableLiveData<Lidar> = MutableLiveData(Lidar())




    suspend fun start() {
        mqtt.start()

    }

    override fun onConnected() {
        println("Lidar MQTT Connected")
        mqtt.subscribe(Lidar.topic)
    }

    override fun connectionLost(cause: Throwable?) {

    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        when (topic) {
            Lidar.topic -> {
                println("MQTT RX $topic")

                val point = gson.fromJson(String(message.payload), LidarPoint::class.java)

                lidar.value?.update(point)
            }
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }

}