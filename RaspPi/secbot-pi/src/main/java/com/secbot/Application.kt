package com.secbot

import com.pi4j.util.Console
import com.secbot.di.DaggerAppComponent
import sun.applet.Main
import java.io.IOException
import java.util.*
import javax.inject.Inject

class Application  {



    @Inject
    lateinit var mainProcess : MainProcess



    companion object {

        private var INSTANCE: Application = Application()





        private var console: Console? = null
        @Throws(InterruptedException::class, IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            DaggerAppComponent.create().inject(INSTANCE)
            console = Console()

            console!!.promptForExit()
            console!!.box("Starting Up Main Program")

            INSTANCE.mainProcess.start()
            while (console!!.isRunning) {
                Thread.sleep(10)
                //  mainProgram.arduinoSerialInterface!!.sendCommand(Command(CommandType.PING.id, "", 0))
            }
        }
    }


}