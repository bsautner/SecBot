package com.secbot.pi.di

import com.hopding.jrpicam.RPiCamera
import com.hopding.jrpicam.enums.Exposure
import com.pi4j.io.serial.Serial
import com.pi4j.io.serial.SerialFactory
import com.pi4j.util.Console
import com.secbot.core.SecBot
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.core.SensorDataHandler
import com.secbot.pi.io.SerialPortManager
import com.secbot.pi.io.SerialWrapper
import com.secbot.pi.io.SimulatorSensorDataHandler
import com.secbot.pi.MainProcess
import com.secbot.core.mqtt.MQTT
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSecBot() : SecBot {
        return SecBot()
    }

    @Provides
    fun provideMainProcess(secBot: SecBot, serialPort: SensorDataHandler, camera: Optional<RPiCamera>, mqtt: MQTT) : MainProcess {
        return MainProcess(secBot, serialPort, mqtt, camera)
    }

    @Provides @Singleton
    fun provideMQTT() : MQTT {
        return MQTT()
    }



    @Provides
    fun provideSerialPort(io: Serial, mqtt: MQTT) : SensorDataHandler {
        return when (SYSTEM) {
            AMD -> SimulatorSensorDataHandler(io)
            PI -> SerialPortManager(io, mqtt)
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