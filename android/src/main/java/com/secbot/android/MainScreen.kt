@file:OptIn(ExperimentalStdlibApi::class, ExperimentalPointerInput::class)

package com.secbot.android

import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.gesture.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.secbot.core.devices.SteeringServo
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun lidarComposable(vm: MainViewModel) {


    val paint = Paint()
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 45f
    paint.color = Color.White.toArgb()


    var  canvasWidth = 0.0F
    var canvasHeight = 0.0F
    var center = Offset(0F, 0F)

    MaterialTheme {
        Column {


            Box(modifier = Modifier.fillMaxWidth().height((vm.screenHeight * .75).dp)) {


            Canvas(modifier = Modifier.fillMaxSize() , onDraw = {
                val obs = vm.getClosestObstacle()
                val padding = 50
                 canvasWidth = size.width - padding
                 canvasHeight = size.height - padding
                 center = this.center



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

            Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
                var center = Offset(0f, 0f)
                Canvas(modifier = Modifier.fillMaxSize().longPressDragGestureFilter(longPressDragObserver = object : LongPressDragObserver {


                    override fun onStop(velocity: Offset) {
                        super.onStop(velocity)
                        vm.dragX = center.x
                        vm.dragY = center.y
                        SteeringServo.turn(SteeringServo.Direction.FORWARD)
                    }
                }).dragGestureFilter(dragObserver = object : DragObserver {


                }).pointerInput {

                           this.detectDragGestures { change, dragAmount ->


                               vm.dragX += dragAmount.x
                               vm.dragY += dragAmount.y

                               if (vm.dragX < center.x - 20) {
                                   SteeringServo.turn(SteeringServo.Direction.LEFT)
                               }
                               else if (vm.dragX > center.x + 20) {
                                   SteeringServo.turn(SteeringServo.Direction.RIGHT)
                               }
                               else {
                                   SteeringServo.turn(SteeringServo.Direction.FORWARD)
                               }

                           }

                }, onDraw = {
                    center = this.center
                    if (vm.dragX.equals(0.0F)) {
                        vm.dragX = this.center.x
                        vm.dragY = this.center.y
                    }
                    drawCircle(Color.Blue, 50F, Offset(vm.dragX, vm.dragY))
                })


        }
    }
}
}









