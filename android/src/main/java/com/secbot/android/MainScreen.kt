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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
    fun canvasDrawExample(vm : MainViewModel) {
        MaterialTheme {
            Column {


                Canvas(modifier = Modifier.size(500.dp, 500.dp)) {
                    drawRect(Color.LightGray, topLeft = Offset(0f, 0f), size = Size(this.size.width, this.size.height))
                    drawCircle(Color.Red, center = Offset(50f, 200f), radius = 40f)





                      rotate(degrees = vm.compass, pivot = Offset((this.size.width / 2), (this.size.height / 2))) {
                        drawRect(Color.Blue, topLeft = Offset((this.size.width / 2) - 100, (this.size.height / 2) -100), size = Size(200F, 200F))
                          drawLine(
                              Color.Red,
                              Offset((this.size.width / 2), (this.size.height / 2)+400),
                              Offset((this.size.width / 2), (this.size.height / 2)),
                              strokeWidth = 5f
                          )
                      }



                    try {
                        vm.lidardata.forEach { (angle, distance) ->

                            // if (lidar.distance.toFloat() < 1000) {
                            rotate(
                                degrees = fixAngle(angle.toFloat(), vm.compass),
                                pivot = Offset((this.size.width / 2), (this.size.height / 2))
                            ) {

                                drawLine(
                                    Color.Green,
                                    Offset((this.size.width / 2), (this.size.height / 2)),
                                    Offset((this.size.width / 2), (this.size.height / 2) + (distance * 10)),
                                    strokeWidth = 5f
                                )
                            }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        println("BEN:: error using lidar data ${ex.message}")
                    }


                  //  }

                }
                Text("Compass Heading ${vm.compass}  ${vm.timestamp}")

                vm.lidardata.forEach { (angle, distance) ->
//                    if (vm.compass == angle) {
//                        Text("LIDAR matched heading $distance")
//                    }
//                    if (vm.compass == angle - vm.compass) {
//                        Text("LIDAR minus heading $distance")
//                    }
//                    if (vm.compass == angle + vm.compass) {
//                        Text("LIDAR plus heading $distance")
//                    }
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







