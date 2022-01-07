package com.secbot.core

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext


abstract class AbstractDevice<T> : Device {
     val scope: DeviceScope = DeviceScope()
     var stopped: Boolean = false

     init {

          stopped = false
     }

     override fun start() {

          val cls = this::class.java.name
          scope.launch(newSingleThreadContext(cls)) {
               println("Starting $cls Thread: ${Thread.currentThread().name}")
               while (!stopped) {
                    delay(10)
               }
          }

     }




     abstract fun update(value: T)

     override fun stop() {
          scope.cancel()
          stopped = true
     }
}