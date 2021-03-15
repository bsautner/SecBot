package com.secbot.android

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.secbot.core.hardware.Sensor
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Composable
fun mainScreen(vm : MainViewModel) {

    MaterialTheme {
        Column {

            Text(vm.test)
            for (s in Sensor.values()  ) {
                Text("${s.name} : ${vm.sensors[s]?.reading}")

            }

            Button(

                onClick = {
                          vm.sendCommand()
                          },
                // Assign reference "button" to the Button composable
                // and constrain it to the top of the ConstraintLayout

            ) {
                Text("Button")
            }


        }

//        Button(onClick = {
//            text = "Hello, ${getPlatformName()}"
//        }) {
//            Text(text)
//        }
    }
}