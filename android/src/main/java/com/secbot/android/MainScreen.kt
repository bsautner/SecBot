package com.secbot.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun lidarComposable(vm: MainViewModel) {
    MaterialTheme {
        Column {
            Canvas(modifier = Modifier.size(500.dp, 500.dp)) {
                drawRect(Color.LightGray, topLeft = Offset(0f, 0f), size = Size(this.size.width, this.size.height))
                drawCircle(Color.Blue, 20F, this.center)


                vm.lidardata.data.forEach { (angle, distance) ->
                    var fixed = angle - vm.compass
                    val rad = fixed * PI / 180
                    val xx = this.center.x + (distance.get() * cos(rad))
                    val yy = this.center.y + (distance.get() * sin(rad))
                  // drawArc(Color.Black, 0f, 2f, useCenter = true, topLeft = Offset(xx.toFloat(), yy.toFloat()), size= Size(distance.toFloat(), distance.toFloat()))
                   drawCircle(Color.Black, 12F, Offset(xx.toFloat(), yy.toFloat()))

                }




            }
            Text("Compass Heading ${vm.compass}  ${vm.timestamp}")
        }
    }
}








