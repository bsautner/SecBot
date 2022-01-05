package com.secbot.pi

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import com.secbot.core.Bus
import com.secbot.core.DeviceScope
import com.secbot.pi.devices.C
import com.secbot.core.Robot
import com.secbot.core.devices.Motor
import com.secbot.core.devices.Sonar
import com.secbot.core.devices.lidar.Lidar
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.devices.led.ArduinoPongLed
import com.secbot.pi.devices.serial.SerialPort
import io.ktor.network.selector.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

@ExperimentalCoroutinesApi
object Program  {

    private val broker = "tcp://localhost:1883"
    val scope = DeviceScope()

    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
//        val gpio: GpioController = GpioFactory.getInstance()
//
//        val motorPin: GpioPinDigitalOutput = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07)
//
//        motorPin.high()

        Bus.deviceListener = PiDeviceListener()
      //  MQTT.broker = broker
      //  MQTT.start()

            runBlocking {
                val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(InetSocketAddress("127.0.0.1", 2323))
                println("Started echo telnet server at ${server.localAddress}")

                while (true) {
                    val socket = server.accept()

                    scope.launch {
                       // println("Socket accepted: ${socket.remoteAddress} ${Thread.currentThread().name}")

                        val input = socket.openReadChannel()
                      //  val output = socket.openWriteChannel(true)
                        val line = input.readUTF8Line(4200)
                        line?.let {
                        //    MQTT.publish(Lidar, line )
                            val values = GsonBuilder().create().fromJson<JsonArray>(it, JsonArray::class.java)
                            println(values.size())
                        }

                      //  println("${line?.length}")
                    //    val obj = GsonBuilder().create().fromJson(line, JsonArray::class.java)
                    // //   println("${obj.size()}")
//
//                        line?.let {
//                         //   println("${socket.remoteAddress}: $it")
//                            output.writeAvailable("echo $it ${System.currentTimeMillis()}".toByteArray())
//
//                        }


                    }
                }
            }
        }

//        val gpio: GpioController = GpioFactory.getInstance()
//
//        val motorPin: GpioPinDigitalOutput = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07)
//
//        motorPin.high()
//
//            Robot.update(SerialPort)
//            Robot.update(MQTT)
//            Robot.update(Sonar)
//            Robot.update(Motor)
//
//            Robot.start()
//
//
//
//        while(true) {
//            Thread.sleep(10)
//        }
//



 //  }


}


