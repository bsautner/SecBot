package com.secbot.di

import com.secbot.MainProcess
import com.secbot.serial.*
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    private val sim = true

    @Provides
    fun provideMainProcess(serialInterface: SerialInterface) : MainProcess {
        return MainProcess(serialInterface)
    }

    @Provides
    fun provideSerialInterface(serialPortListener: SerialPortListener) : SerialInterface {
        return if (sim) {
            SimulatedSerialInterface(serialPortListener)
        } else {
            ArduinoSerialInterface(serialPortListener)

        }
    }

    @Provides
    fun provideSerialPortListener() : SerialPortListener {
        return if (sim) {
            SimulatedSerialPortListener()
        } else {
            DefaultSerialPortListener()

        }
    }
}