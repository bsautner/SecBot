package com.secbot.pi

import com.secbot.core.DeviceListener
import com.secbot.pi.devices.C
import com.secbot.pi.devices.Robot
import kotlinx.coroutines.*
import java.io.IOException

@ExperimentalCoroutinesApi
object Program {







    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {


        C.promptForExit()
        C.box("Starting Up Main Program")

        CoroutineScope(Dispatchers.IO).launch {
            runCatching{
                C.print("Starting Robot")
                Robot.start(PiDeviceListener())
            }
        }

        while (Robot.stopped.not()) {
            Thread.sleep(100)
        }




    }
}


