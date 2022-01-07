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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.devices.lidar.LidarPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class LidarActivity : ComponentActivity() {

    val vm by viewModels<LidarViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayMetrics: DisplayMetrics =  resources.displayMetrics
        vm.screenHeight = displayMetrics.heightPixels / displayMetrics.density
        vm.screenWidth = displayMetrics.widthPixels / displayMetrics.density
        setContent {
            lidarComposable(vm)
//            SecbotTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting(vm, vm.live.value ?: "null")
//                }
//            }
        }

        val observer = Observer<String> {
            //println("BEN:: $it")

        }

        val lidarObserver = Observer<Lidar> {
            //println("BEN:: $it")

        }
        vm.live.observe(this, observer)
        vm.lidar.observe(this, lidarObserver)


    }

    val r = Random()
    suspend fun tick() {
        while (true) {
            vm.live.postValue(System.currentTimeMillis().toString())
           // println("BEN:: tick")
          //  vm.lidar.value?.update(LidarPoint(r.nextInt(360).toDouble(), r.nextInt(1000).toDouble()))
             delay(100)
        }
    }

    override fun onResume() {
        super.onResume()
        vm.live.postValue("foo")
        GlobalScope.launch {
            vm.start()
            tick()

        }


    }
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