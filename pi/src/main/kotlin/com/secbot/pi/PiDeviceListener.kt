package com.secbot.pi

import com.secbot.core.*
import com.secbot.core.devices.InfraredRange
import com.secbot.pi.devices.led.ArduinoPongLed
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.devices.mag.MagnetometerAccelerometer
import com.secbot.pi.devices.serial.SerialPort
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PiDeviceListener : DeviceListener {

    private val scope = DeviceScope()

    override suspend fun onReceive(data: String) {
      //  println(data)
        val split = data.split(',')

        when (Source.valueOf(split[0])) {
            Source.LDR -> {
              Lidar.update(split)
                publish(Lidar, data)

            }
            Source.MAG_SERIAL -> {
             //   MagnetometerAccelerometer.process(data)
            }
            Source.MAG_PI -> {
              //  MagnetometerAccelerometer.process(data)
                            }
            Source.ACC_PI -> {}
            Source.CMP -> {}
            Source.PING -> {

            }
            Source.PONG -> {


                coroutineScope {
                    ArduinoPongLed.update(100)
                    delay(1000)

                    SerialPort.update("${Source.PING}")
                }

            }
            Source.MQTT -> {

            }
            Source.ACC_SERIAL -> TODO()
            Source.STEER -> {
                  SerialPort.update(data)
            }
            Source.MOTOR -> {
                val motion = Motion.valueOf(split[1])
                motion.let {
                    SerialPort.update(data)
                }
            }
            Source.FORWARD_IR ->  {
                InfraredRange.update(split[1].toFloat())
                publish(InfraredRange, data)
            }
          }
        }

        private fun publish(device: Device, payload: String) {
            scope.launch {
                MQTT.publish(device, payload)
            }


        }
    }