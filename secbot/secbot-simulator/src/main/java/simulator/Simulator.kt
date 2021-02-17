package simulator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.dispatch.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.secbot.control.SecBot
import kotlin.random.Random

class Simulator {

    private var previousTimeNanos: Long = Long.MAX_VALUE
    private val colors = arrayOf(
        Color.Red, Color.Blue, Color.Cyan,
        Color.Magenta, Color.Yellow, Color.Black
    )
    private var startTime = 0L

    var size by mutableStateOf(IntSize(0, 0))

    var pieces = mutableStateListOf<Ball.BallViewModel>()
        private set

    val secBot = SecBot()

    var bot = mutableStateOf(SecBotSimulator.BotData(this, secBot))

    var elapsed by mutableStateOf(0L)

    var clicked by mutableStateOf(0)


    var numBlocks by mutableStateOf(20)



    private fun start() {
        previousTimeNanos = System.nanoTime()
        startTime = previousTimeNanos
        clicked = 0

        pieces.clear()



        repeat(numBlocks) { index ->
            pieces.add(Ball.BallViewModel(this, index * 1.5f + 15f, colors[index % colors.size]).also { piece ->
                piece.position = Random.nextDouble(0.0, 100.0).toFloat()
            })
        }


    }




    private fun update(nanos: Long) {
        val dt = (nanos - previousTimeNanos).coerceAtLeast(0)
        previousTimeNanos = nanos
        elapsed = nanos - startTime

        pieces.forEach { it.update(dt) }


       bot.value.update(dt)
    }

    fun clicked(piece: Ball.BallViewModel) {

        clicked++

    }

    @Composable
    fun simulatorComposable() {
        val game = remember { Simulator() }
        val robot = remember { SecBotSimulator() }
        val ball = remember { Ball() }
        game.start()
        Row {


            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .onSizeChanged {
                    game.size = it
                }
            ) {
                game.pieces.forEachIndexed { index, piece -> ball.ballComposable(index, piece) }

                robot.robotComposable(game.bot.value)

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
}



