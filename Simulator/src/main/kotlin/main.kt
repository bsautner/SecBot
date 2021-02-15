package org.jetbrains.compose.demo.falling

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize

fun main() =
    Window(title = "Falling Balls", size = IntSize(1200, 1200)) {
         spaceComposable()

    }

