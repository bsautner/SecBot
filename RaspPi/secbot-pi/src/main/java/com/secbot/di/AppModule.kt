package com.secbot.di

import com.pi4j.util.Console
import com.secbot.MainProcess
import com.secbot.serial.*
import dagger.Module
import dagger.Provides

@Module
class AppModule {


    @Provides
    fun provideMainProcess(serialPort: SerialPortIO) : MainProcess {
        return MainProcess(serialPort)
    }



    @Provides
    fun provideSerialPort() : SerialPortIO {
        return SerialPortIO()

    }

    @Provides
    fun provideConsole() : Console {
        return Console()
    }

}