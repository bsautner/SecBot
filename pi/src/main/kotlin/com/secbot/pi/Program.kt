package com.secbot.pi

import com.secbot.core.DeviceListener
import com.secbot.pi.devices.C
import com.secbot.pi.devices.Robot
import kotlinx.coroutines.*
import java.io.IOException

@ExperimentalCoroutinesApi
object Program {




    var listener = object : DeviceListener {
        override suspend fun onReceive(data: String) {
          //  C.print(data)
        }

    }


    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {


        C.promptForExit()
        C.box("Starting Up Main Program")

        CoroutineScope(Dispatchers.IO).launch {
            runCatching{
                C.print("Starting Robot")
                Robot.start(listener)
            }
        }

        while (Robot.stopped.not()) {
            Thread.sleep(100)
        }




    }
}


