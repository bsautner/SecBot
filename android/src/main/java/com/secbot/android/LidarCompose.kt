package com.secbot.android


import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.google.gson.JsonArray
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@ExperimentalComposeUiApi
@Composable
fun lidarComposable(vm: LidarViewModel) {

    val t: String by vm.live.observeAsState("")
    val payload: JsonArray by vm.payload.observeAsState(JsonArray())
    val paint = Paint()
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 45f
    paint.color = Color.White.toArgb()


    val touch: Offset by vm.touch.observeAsState(Offset(0F, 0F))


    var canvasWidth: Float
    var canvasHeight: Float

    MaterialTheme {
        Column {

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(vm.screenHeight.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->

                        change.consumeAllChanges()
                        vm.touch.postValue(change.position)


                    }


                }
                .pointerInteropFilter {
                    // println("BEN:: ${it}")

                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {}
                        MotionEvent.ACTION_MOVE -> {}
                        MotionEvent.ACTION_UP -> {
                            vm.touch.postValue(vm.center)
                        }
                        else -> false
                    }
                    true
                }
            )
            {
                Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                    val obs = 10//vm.getClosestObstacle()
                    val ir = 100//vm.infraredRange.rangeCm
                    val son = 100//vm.sonar.rangeCm
                    val padding = 50
                    canvasWidth = size.width - padding
                    canvasHeight = size.height - padding

                    val botColor: () -> Color = {
                        if (obs < 20) {
                            Color.Red
                        } else {
                            Color.Blue
                        }
                    }
                    vm.center = this.center
                    drawRect(Color.Black, topLeft = Offset(0f, 0f), size = Size(this.size.width, this.size.height))
                    drawCircle(botColor.invoke(), 50F, this.center)
                    if (vm.touch.value == null) {
                        vm.touch.value = this.center
                    }
                    drawCircle(Color.Blue, 60F, touch)

                    drawLine(
                        botColor.invoke(),
                        this.center,
                        Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2),
                        strokeWidth = 10f
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        "${vm.compass.toInt()}\u00B0",
                        this.center.x,
                        (canvasHeight / 2 - canvasWidth / 2) + padding / 2 - 20,
                        paint
                    )

                    val north = 360 - vm.compass
                    rotate(north) {
                        drawLine(
                            Color.White,
                            this.center,
                            Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2),
                            strokeWidth = 5f
                        )
                        drawCircle(Color.White, canvasWidth / 2, this.center, style = Stroke(5f))
                        drawContext.canvas.nativeCanvas.drawText(
                            "N",
                            this.center.x,
                            (canvasHeight / 2 - canvasWidth / 2) + padding / 2 - 20,
                            paint
                        )
                    }

                    rotate(180 - vm.compass) {
                        drawLine(
                            Color.White,
                            this.center,
                            Offset(this.center.x, (canvasHeight / 2 - canvasWidth / 2) + padding / 2),
                            strokeWidth = 5f
                        )
                        drawContext.canvas.nativeCanvas.drawText(
                            "S",
                            this.center.x,
                            (canvasHeight / 2 - canvasWidth / 2) + padding / 2 - 20,
                            paint
                        )
                    }

                    val obstructions: MutableList<Offset> = ArrayList()
                    val surroundings: MutableList<Offset> = ArrayList()



                    payload.forEach {
                        val item = it.asJsonArray
                        val angle: Double = item[1].asDouble
                        val distance: Double = item[2].asDouble
                        val fixed = angle - 90//vm.compass
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
                        "$t clearance: Sonar: ${son.toInt()}cm IR: ${ir.toInt()}cm ${obs.toInt()}mm",
                        canvasWidth / 2,
                        canvasHeight - 20,
                        paint
                    )
                })
            }
        }


    }
}










