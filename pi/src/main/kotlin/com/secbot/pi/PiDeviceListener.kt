package com.secbot.pi

import com.secbot.core.DeviceListener
import com.secbot.core.Source
import com.secbot.pi.devices.led.ArduinoPongLed
import com.secbot.core.devices.lidar.Lidar
import com.secbot.pi.devices.mag.MagnetometerAccelerometer
import com.secbot.pi.devices.serial.SerialPort
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class PiDeviceListener : DeviceListener {
    override suspend fun onReceive(data: String) {
        println(data)
        val split = data.split(',')

        when (Source.valueOf(split[0])) {
            Source.LDR -> {
              Lidar.update(split, true)
            }
            Source.MAG_SERIAL -> {
                MagnetometerAccelerometer.process(data)
            }
            Source.MAG_PI -> {
                MagnetometerAccelerometer.process(data)
                            }
            Source.ACC_PI -> {}
            Source.CMP -> {}
            Source.PING -> {

            }
            Source.PONG -> {


                coroutineScope {
                    ArduinoPongLed.blink(100)
                    delay(1000)

                    SerialPort.enqueue("${Source.PING}")
                }

            }
            Source.MQTT -> {

            }
            Source.ACC_SERIAL -> TODO()
            Source.STEER -> {
                  SerialPort.enqueue(data)
            }
        }
        }
    }