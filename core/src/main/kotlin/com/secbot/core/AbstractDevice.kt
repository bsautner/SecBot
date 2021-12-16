package com.secbot.core

import kotlinx.coroutines.cancel

abstract class AbstractDevice {
     val scope: DeviceScope = DeviceScope()
     lateinit var deviceListener: DeviceListener
     var stopped: Boolean = false

     open suspend fun start(deviceListener: DeviceListener) {
          this.deviceListener = deviceListener
          println("Starting ${this::class.java.name} Thread: ${Thread.currentThread().name}")
          stopped = false

     }
     open fun stop() {
          scope.cancel()
          stopped = true
     }
}