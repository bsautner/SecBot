package simulator

import androidx.compose.foundation.background
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
import com.secbot.control.SecBot

class SecBotSimulator {



    @Composable
    fun robotComposable(bot: BotData) {
        val boxSize = 60.dp
        Box(
            Modifier
                .offset(bot.offset.dp, bot.position.dp)
                .shadow(30.dp)
                .clip(CircleShape)
        ) {
            Box(
                Modifier
                    .size(boxSize, boxSize)
                    .background(Color.Gray)

            )
        }
    }


    data class BotData(val simulator: Simulator, val secBot: SecBot) {

        var position by mutableStateOf(600F)
        var offset by mutableStateOf(600F)



        fun update(dt: Long) {

            val delta = (dt / 1E8 * secBot.speed).toFloat()

            if (secBot.forwardSonarDistanceCm > 0) {
                offset = if (offset < simulator.size.width) offset + delta else offset
            }



        }


    }
}

