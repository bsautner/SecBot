package com.secbot.pi.devices.led

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import com.secbot.core.AbstractDevice
import com.secbot.core.DeviceListener
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

object ArduinoPongLed : AbstractDevice() {
    private val gpio: GpioController = GpioFactory.getInstance()

    private val ledPin: GpioPinDigitalOutput = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25)

    override suspend fun start(deviceListener: DeviceListener) {
        super.start(deviceListener)
        blink(2000)
    }

    suspend fun blink(delay: Long) {
        scope.async {
            ledPin.high()
            delay(delay)
            ledPin.low()
        }.start()
    }

//    scope.launch {

//    }


}