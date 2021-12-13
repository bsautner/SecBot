package com.secbot.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.secbot.core.devices.lidar.Lidar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun lidarComposable(vm: MainViewModel) {

    val maxRelevantAge = 5000

    MaterialTheme {
        Column {
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                val padding = 50
                val canvasWidth = size.width - padding
                val canvasHeight = size.height - padding

                drawRect(Color.LightGray, topLeft = Offset(0f, 0f), size = Size(this.size.width, this.size.height))
                drawCircle(Color.Blue, 20F, this.center)

                val north = 360 - vm.compass
                rotate(north) {
                    drawLine(
                        Color.Blue,
                        this.center,
                        Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2)
                    )
                    drawCircle(Color.Blue, canvasWidth / 2, this.center, style = Stroke(5f))
                }

                rotate(vm.compass) {
                    drawLine(
                        Color.Red,
                        this.center,
                        Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2)
                    )
                }

                    val obstructions: MutableList<Offset> = ArrayList()
                    val surroundings: MutableList<Offset> = ArrayList()
                    vm.lidardata.data
                    vm.lidardata.data.forEach { (a, d) ->

                        if (System.currentTimeMillis() - d.timestamp.get() < maxRelevantAge) {
                            val angle: Double = a.toDouble() / Lidar.relevance
                            val distance: Double = ((d.distance.get() / Lidar.relevance).toDouble() / 2)
                            val fixed = angle - vm.compass
                            val rad = fixed * PI / 180
                            val xx = this.center.x + (distance * cos(rad))
                            val yy = this.center.y + (distance * sin(rad))

                            if (distance > 0 && ((angle < 20) or (angle > 340))) {
                                obstructions.add(Offset(xx.toFloat(), yy.toFloat()))
                            } else if (distance < canvasWidth / 2) {
                                surroundings.add(Offset(xx.toFloat(), yy.toFloat()))
                            }

                        }
                    }


                    drawPoints(obstructions, PointMode.Points, Color.Red, strokeWidth = 5f)
                    drawPoints(surroundings, PointMode.Points, Color.Black, strokeWidth = 5f)



            })

        }
    }
}









