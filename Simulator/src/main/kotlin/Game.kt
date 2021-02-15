package org.jetbrains.compose.demo.falling

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.dispatch.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class Game {
    private var previousTimeNanos: Long = Long.MAX_VALUE
    private val colors = arrayOf(
        Color.Red, Color.Blue, Color.Cyan,
        Color.Magenta, Color.Yellow, Color.Black
    )
    private var startTime = 0L

    var size by mutableStateOf(IntSize(0, 0))

    var pieces = mutableStateListOf<PieceData>()
        private set


    var bot = mutableStateOf(BotData(this, 20F, Color.Black))

    var elapsed by mutableStateOf(0L)

    var clicked by mutableStateOf(0)



    var numBlocks by mutableStateOf(20)

    fun start() {
        previousTimeNanos = System.nanoTime()
        startTime = previousTimeNanos
        clicked = 0

        pieces.clear()



        repeat(numBlocks) { index ->
            pieces.add(PieceData(this, index * 1.5f + 15f, colors[index % colors.size]).also { piece ->
                piece.position = Random.nextDouble(0.0, 100.0).toFloat()
            })
        }

       // bot.value.position = 400F

    }


    fun update(nanos: Long) {
        val dt = (nanos - previousTimeNanos).coerceAtLeast(0)
        previousTimeNanos = nanos
        elapsed = nanos - startTime

        pieces.forEach { it.update(dt) }


        bot.value.update(dt)
    }

    fun clicked(piece: PieceData) {

        clicked++

    }
}

@Composable
fun spaceComposable() {
    val game = remember { Game() }
    game.start()
    Row {


        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .onSizeChanged {
                game.size = it
            }
        ) {
            game.pieces.forEachIndexed { index, piece -> piece(index, piece) }

            bot(game.bot.value)

          //  game.bots.forEachIndexed { index, bot -> bot(bot)  }
        }


        LaunchedEffect(Unit) {
            while (true) {
                withFrameNanos {

                    game.update(it)

                }
            }
        }
    }
}

