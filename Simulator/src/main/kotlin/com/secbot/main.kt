package com.secbot

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize

fun main() =
    Window(title = "SecBot Simulator", size = IntSize(1200, 1200)) {
        Simulator().simulatorComposable()

    }

