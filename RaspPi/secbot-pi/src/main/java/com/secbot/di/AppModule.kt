package com.secbot.di

import com.hopding.jrpicam.RPiCamera
import com.hopding.jrpicam.enums.Exposure
import com.pi4j.io.serial.Serial
import com.pi4j.io.serial.SerialFactory
import com.pi4j.util.Console
import com.secbot.Const.PHOTO_PATH
import com.secbot.IO.IO
import com.secbot.IO.SerialPortIO
import com.secbot.IO.SerialWrapper
import com.secbot.IO.SimulatorIO
import com.secbot.MainProcess
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@Module
class AppModule {


    @Provides
    fun provideMainProcess(serialPort: IO, camera: Optional<RPiCamera>) : MainProcess {
        return MainProcess(serialPort, camera)
    }



    @Provides
    fun provideSerialPort(io: Serial) : IO {
        return when (SYSTEM) {
            AMD -> SimulatorIO(io)
            PI -> SerialPortIO(io)
            else ->  throw RuntimeException()
        }

    }

    @Provides
    fun provideConsole() : Console {
        return Console()
    }

    @Provides
    fun provideIO() : Serial {

        return when (SYSTEM) {
             AMD -> SerialWrapper()
             PI -> SerialFactory.createInstance()
             else ->  throw RuntimeException()
        }

    }

    @Provides
    fun provideCamera() : Optional<RPiCamera> {


        return when (SYSTEM) {
            AMD ->  Optional.empty()
            PI -> {
                val piCamera = RPiCamera(PHOTO_PATH)
                    .setWidth(300).setHeight(200)
                    .setBrightness(40)
                    .setExposure(Exposure.AUTO)
                    .setContrast(50).setAddRawBayer(false)
                    .setQuality(75)
                    // .setVerticalFlipOn()
                    // .setRegionOfInterest(0.5, 0.5, 0.25, 0.25)
                    .setTimeout(1000)

                piCamera.setFullPreviewOn()

                Optional.of(piCamera)
            }
            else ->  throw RuntimeException()
        }



    }

    companion object {
        private var SYSTEM = System.getProperty("os.arch")
        private const val PI = "arm"
        private const val AMD =  "amd64"

    }

}