package com.secbot.android

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.secbot.android.Compass.CompassListener
import com.secbot.core.DeviceScope
import com.secbot.core.Robot
import com.secbot.core.devices.InfraredRange
import com.secbot.core.devices.Sonar
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async


@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val scope: DeviceScope = DeviceScope()
    private val vm by viewModels<MainViewModel>()
    private lateinit var compass: Compass
    private val broker = "tcp://10.0.0.205:1883"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MQTT.broker = broker

      //  MQTT.topics.add(InfraredRange)
       // MQTT.topics.add(Lidar)
        setupCompass()
        val displayMetrics: DisplayMetrics =  resources.displayMetrics
        vm.screenHeight = displayMetrics.heightPixels / displayMetrics.density
        vm.screenWidth = displayMetrics.widthPixels / displayMetrics.density
        setContent {
            lidarComposable(vm)
        }

    }

    override fun onResume() {
        super.onResume()
        println("BEN::channel opening")
        compass.start()
        Robot.update(Sonar)
        Robot.update(Lidar)
        Robot.update(InfraredRange)
        MQTT.start()



        println("BEN:: channel closed")
    }

    override fun onPause() {
        super.onPause()
        compass.stop()
    }


    private fun setupCompass() {
        compass = Compass(this)
        val cl: CompassListener = getCompassListener()
        compass.setListener(cl)
    }

    private fun getCompassListener(): CompassListener {
        return object : CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
           //     println("Compass: $azimuth")
                vm.compass = azimuth
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MQTT.stop()
    }




}