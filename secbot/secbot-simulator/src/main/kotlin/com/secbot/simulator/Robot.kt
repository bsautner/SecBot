package com.secbot.simulator

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class Robot {



    @Composable
    fun robotComposable(bot: BotData) {
        val boxSize = 60.dp
        Box(
            Modifier
                .offset(bot.offset.dp, bot.position.dp)
                //.shadow(30.dp)
                .clip(CircleShape)
        ) {
            Box(
                Modifier
                    .size(boxSize, boxSize)
                    .background(if (bot.clicked) Color.Gray else bot.color)
                    .clickable(onClick = { bot.click() })
            )
        }
    }


    data class BotData(val simulator: Simulator, val velocity: Float, val color: Color) {
        var clicked by mutableStateOf(false)
        var position by mutableStateOf(600F)
        var offset by mutableStateOf(600F)
        private var forwardDistanceCm = 600


        fun update(dt: Long) {

            val delta = (dt / 1E8 * velocity).toFloat()

            if (forwardDistanceCm > 0) {
                offset = if (offset < simulator.size.width) offset + delta else offset
            }



           // position = if (position < simulator.size.height) position + delta else 0f
            println(delta)

        }

        fun click() {
            if (!clicked) {
                clicked = true
                // game.clicked(this)
            }
        }
    }
}

