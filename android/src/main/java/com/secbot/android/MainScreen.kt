package com.secbot.android

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.secbot.core.hardware.Sensor

@Composable
fun mainScreen(vm : MainViewModel) {

    MaterialTheme {
        Column {
            Text(vm.test)
            for (s in Sensor.values()  ) {
                Text("${s.name} : ${vm.sensors[s]?.reading}")

            }


        }

//        Button(onClick = {
//            text = "Hello, ${getPlatformName()}"
//        }) {
//            Text(text)
//        }
    }
}