package com.secbot.android

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.secbot.common.getPlatformName

@Composable
fun mainScreen(vm : MainViewModel) {

    MaterialTheme {
        Column {
            Text(vm.txt)
            Text(vm.txt)
        }

//        Button(onClick = {
//            text = "Hello, ${getPlatformName()}"
//        }) {
//            Text(text)
//        }
    }
}