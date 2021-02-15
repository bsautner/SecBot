package org.jetbrains.compose.demo.falling

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
import org.jetbrains.skija.Canvas

@Composable
fun bot(bot: BotData) {
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



data class BotData(val game: Game, val velocity: Float, val color: Color) {
    var clicked by mutableStateOf(false)
    var position by mutableStateOf(600F)
    var offset by mutableStateOf(600F)



    fun update(dt: Long) {

        val delta = (dt / 1E8 * velocity).toFloat()



        position = if (position < game.size.height) position + delta else 0f
        offset = if (offset < game.size.width) offset + delta else 0f
        println(position)
    }

    fun click() {
        if (!clicked) {
            clicked = true
           // game.clicked(this)
        }
    }
}

