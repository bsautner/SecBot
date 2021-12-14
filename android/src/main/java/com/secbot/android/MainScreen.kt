@file:OptIn(ExperimentalStdlibApi::class)

package com.secbot.android

import android.graphics.Paint
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun lidarComposable(vm: MainViewModel) {


    val paint = Paint()
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 45f
    paint.color = Color.White.toArgb()

    MaterialTheme {
        Column {
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                val obs = vm.getClosestObstacle()
                val padding = 50
                val canvasWidth = size.width - padding
                val canvasHeight = size.height - padding

                val botColor : () -> Color = {
                    if (obs < 20) {  Color.Red} else { Color.Blue }
                }

                drawRect(Color.Black, topLeft = Offset(0f, 0f), size = Size(this.size.width, this.size.height))
                drawCircle(botColor.invoke(), 20F, this.center)

                    drawLine(
                        botColor.invoke(),
                        this.center,
                        Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2),
                        strokeWidth = 10f
                    )
                drawContext.canvas.nativeCanvas.drawText("${vm.compass.toInt()}\u00B0", this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2 - 20, paint )

                val north = 360 - vm.compass
                rotate(north) {
                    drawLine(
                        Color.White,
                        this.center,
                        Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2),
                                strokeWidth = 5f
                    )
                    drawCircle(Color.White, canvasWidth / 2, this.center, style = Stroke(5f))
                    drawContext.canvas.nativeCanvas.drawText("N", this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2 - 20, paint )
                }

                rotate(180 - vm.compass) {
                    drawLine(
                        Color.White,
                        this.center,
                        Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2),
                        strokeWidth = 5f
                    )
                    drawContext.canvas.nativeCanvas.drawText("S", this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2 - 20, paint )
                }

                    val obstructions: MutableList<Offset> = ArrayList()
                    val surroundings: MutableList<Offset> = ArrayList()
                    vm.lidar.data
                    vm.lidar.data.values.filter {
                        (System.currentTimeMillis() - it.timestamp < vm.maxRelevantAge)
                    }.forEach{

                            val angle: Double = it.angle
                            val distance: Double = it.distance
                            val fixed = angle - vm.compass
                            val rad = fixed * PI / 180
                            val xx = this.center.x + (distance * cos(rad))
                            val yy = this.center.y + (distance * sin(rad))

                            if (distance > 0 && ((angle < 10) or (angle > 350))) {
                                obstructions.add(Offset(xx.toFloat(), yy.toFloat()))

                            } else { // if (distance < canvasWidth / 2) {
                                surroundings.add(Offset(xx.toFloat(), yy.toFloat()))
                            }

                        }



                    drawPoints(obstructions, PointMode.Points, Color.Red, strokeWidth = 15f)
                    drawPoints(surroundings, PointMode.Points, Color.Cyan, strokeWidth = 15f)


                drawContext.canvas.nativeCanvas.drawText(
                    "clearance: ${obs.toInt()}mm", canvasWidth / 2, canvasHeight-20, paint
                )
            })
        }
    }
}









