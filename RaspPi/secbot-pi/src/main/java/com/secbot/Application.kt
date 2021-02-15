package com.secbot

import com.pi4j.system.SystemInfo
import com.pi4j.util.Console
import com.secbot.di.DaggerAppComponent
import kotlinx.coroutines.*
import sun.applet.Main
import java.io.IOException
import java.util.*
import javax.inject.Inject

class Application  {



    @Inject
    lateinit var mainProcess : MainProcess

    @Inject
    lateinit var console: Console



    companion object {

        private var INSTANCE: Application = Application()

        @Throws(InterruptedException::class, IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {

           System.getProperties().stringPropertyNames().forEach {
               println(it + " : " + System.getProperty(it))
           }


            DaggerAppComponent.create().inject(INSTANCE)

            INSTANCE.console.promptForExit()
            INSTANCE.console.box("Starting Up Main Program")

             runBlocking {
                INSTANCE.mainProcess.start()
             }

        }
    }


}