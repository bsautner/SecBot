package com.secbot.pi

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.Console
import com.secbot.pi.di.DaggerAppComponent
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class Program  {



    @Inject
    lateinit var mainProcess : MainProcess

    @Inject
    lateinit var console: Console



    companion object {

        private var INSTANCE: Program = Program()


        @Throws(InterruptedException::class, IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            println("hello world")

//
            DaggerAppComponent.create().inject(INSTANCE)


            INSTANCE.console.promptForExit()
            INSTANCE.console.box("Starting Up Main Program")

             runBlocking {

                INSTANCE.mainProcess.start()
             }

        }
    }


}