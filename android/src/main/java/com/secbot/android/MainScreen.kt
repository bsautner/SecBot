package com.secbot.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate

@ExperimentalCoroutinesApi
@Composable
fun mainScreen(vm : MainViewModel) {
    val rectSize = 40.dp
    val modifier = Modifier.clipToBounds()

    Canvas(modifier = modifier) {
        translate(size.width / 2, size.height / 2) {
            drawRect(
                brush = SolidColor(Color.Black),
                Offset(-(rectSize / 2).toPx(), -(rectSize / 2).toPx()),
                Size(rectSize.toPx(), rectSize.toPx()),
                style = Stroke(width = 2f)
            )
        }
    }
}

//    MaterialTheme {
//        Column {
//
//            Text(vm.test)
//
//            Text("Compass Heading ${vm.compass.heading}")
//
//
//
//            Button(
//
//                onClick = {
//                          vm.sendCommand()
//                          },
//                // Assign reference "button" to the Button composable
//                // and constrain it to the top of the ConstraintLayout
//
//            ) {
//                Text("Button")
//            }
//
//
//
//
//        }
//
////        Button(onClick = {
////            text = "Hello, ${getPlatformName()}"
////        }) {
////            Text(text)
////        }
//    }

//    @Preview(showBackground = true)
    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    fun canvasDrawExample(vm : MainViewModel) {
        MaterialTheme {
            Column {


                Canvas(modifier = Modifier.size(500.dp, 500.dp)) {
                    drawRect(Color.LightGray, topLeft = Offset(0f, 0f), size = Size(this.size.width, this.size.height))
                    drawCircle(Color.Red, center = Offset(50f, 200f), radius = 40f)





                      rotate(degrees = vm.compass.heading.toFloat(), pivot = Offset((this.size.width / 2), (this.size.height / 2))) {
                        drawRect(Color.Blue, topLeft = Offset((this.size.width / 2) - 100, (this.size.height / 2) -100), size = Size(200F, 200F))
                          drawLine(
                              Color.Red,
                              Offset((this.size.width / 2), (this.size.height / 2)+400),
                              Offset((this.size.width / 2), (this.size.height / 2)),
                              strokeWidth = 5f
                          )
                      }


                    vm.lidardata.forEach { (angle, lidar) ->

                       // if (lidar.distance.toFloat() < 1000) {
                            rotate(
                                degrees = fixAngle(angle.toFloat(), vm.compass.heading.toFloat()),
                                pivot = Offset((this.size.width / 2), (this.size.height / 2))
                            ) {

                                drawLine(
                                    Color.Green,
                                    Offset((this.size.width / 2), (this.size.height / 2)),
                                    Offset((this.size.width / 2), (this.size.height / 2) + lidar.distance.toFloat()),
                                    strokeWidth = 5f
                                )
                            }
                        }


                  //  }

                }
                Text("Compass Heading ${vm.compass.heading.toInt()}")

                vm.lidardata.forEach { (angle, lidar) ->
                    if (vm.compass.heading.toInt() == angle) {
                        Text("LIDAR matched heading ${lidar.distance}")
                    }
                    if (vm.compass.heading.toInt() == angle - vm.compass.heading.toInt()) {
                        Text("LIDAR minus heading ${lidar.distance}")
                    }
                    if (vm.compass.heading.toInt() == angle + vm.compass.heading.toInt()) {
                        Text("LIDAR plus heading ${lidar.distance}")
                    }
                }

            }
        }
    }

fun fixAngle(orig: Float, dir: Float) : Float {
    if (orig + dir <= 360) {
        return orig + dir
    }
    else {
        return (orig + dir) - 360
    }
}







