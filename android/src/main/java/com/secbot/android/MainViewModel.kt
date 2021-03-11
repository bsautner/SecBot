package com.secbot.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


class MainViewModel  : ViewModel() {

    var txt by mutableStateOf("foo")
        private set


    fun setValue(s : String) {
        txt = s

    }
}