package com.secbot.core


import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.IOException

import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
object Robot : AbstractDevice<Device>() {

    private val devices : MutableList<Device> = ArrayList()



    @Throws(IOException::class, InterruptedException::class)
    override fun start() {
        super.start()

    }

    override fun stop() {
        devices.forEach {
            println("Stopping: ${it::class.java.name}")
            it.stop()
        }
    }

    override fun update(payload: Device) {
        devices.add(payload)
        println("Registered ${payload::class.java.name}")

    }


}


