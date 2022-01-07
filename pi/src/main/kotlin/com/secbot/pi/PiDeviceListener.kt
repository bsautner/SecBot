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

    override suspend fun onReceive(topic: String, data: String) {

    }
}