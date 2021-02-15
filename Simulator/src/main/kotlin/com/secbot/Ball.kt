package com.secbot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class Ball {
    @Composable
    fun ballComposable(index: Int, piece: BallViewModel) {
        val boxSize = 10.dp
        Box(
            Modifier
                .offset(boxSize * index * 5 / 3, piece.position.dp)
                .shadow(30.dp)
                .clip(CircleShape)
        ) {
            Box(
                Modifier
                    .size(boxSize, boxSize)
                    .background(if (piece.clicked) Color.Gray else piece.color)
                    .clickable(onClick = { piece.click() })
            )
        }
    }


    data class BallViewModel(val simulator: Simulator, val velocity: Float, val color: Color) {
        var clicked by mutableStateOf(false)
        var position by mutableStateOf(0f)

        fun update(dt: Long) {
            if (clicked) return
            val delta = (dt / 1E8 * velocity).toFloat()
            position = if (position < simulator.size.height) position + delta else 0f
        }

        fun click() {
            if (!clicked) {
                clicked = true
                simulator.clicked(this)
            }
        }
    }
}

