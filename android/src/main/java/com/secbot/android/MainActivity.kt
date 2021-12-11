package com.secbot.android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.secbot.android.Compass.CompassListener
import com.secbot.core.DeviceScope
import com.secbot.core.mqtt.MQTT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    val scope: DeviceScope = DeviceScope()
    private val vm by viewModels<MainViewModel>()


        private lateinit var compass: Compass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCompass()
        setContent {
          //  mainScreen(vm)
            lidarComposable(vm)
        }

    }

    override fun onResume() {
        super.onResume()
        println("BEN::channel opening")
        compass.start()
        scope.async {
            MQTT.start(AndroidDeviceListener(vm))
        }.start()



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
             //   println("Compass: $azimuth")
                vm.compass = azimuth
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MQTT.stop()
    }




}