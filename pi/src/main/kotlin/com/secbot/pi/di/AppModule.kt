package com.secbot.pi.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hopding.jrpicam.RPiCamera
import com.hopding.jrpicam.enums.Exposure
import com.pi4j.io.serial.Serial
import com.pi4j.io.serial.SerialFactory
import com.pi4j.util.Console
import com.secbot.core.SecBot
import com.secbot.core.SensorDataProcessor
import com.secbot.pi.Const.PHOTO_PATH
import com.secbot.pi.io.SensorSerialPortManager
import com.secbot.pi.MainProcess
import com.secbot.core.mqtt.MQTT
import com.secbot.pi.io.DeviceSerialPortManager
import com.secbot.pi.io.SerialManager
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Named
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
    fun provideMainProcess(secBot: SecBot, @Named(SENSOR_SERIAL_PORT) sensorPort: SerialManager, @Named(DEVICE_COMMAND_SERIAL_PORT) commandPort: SerialManager, camera: Optional<RPiCamera>, mqtt: MQTT) : MainProcess {
        return MainProcess(secBot, sensorPort, commandPort, mqtt, camera)
    }

    @Provides @Singleton
    fun provideMQTT() : MQTT {
        return MQTT()
    }

    @Provides @Reusable
    fun provideGson() : Gson {

        return GsonBuilder().create()
    }

    @Provides @Reusable
    fun provideSensorDataProcessor(gson: Gson) : SensorDataProcessor {
        return SensorDataProcessor(gson)
    }

    @Provides @Named(SENSOR_SERIAL_PORT)
    fun provideSensorSerialPort(sensorDataProcessor: SensorDataProcessor, io: Serial, mqtt: MQTT, gson: Gson) : SerialManager {
        return SensorSerialPortManager(sensorDataProcessor, io, mqtt, SENSOR_SERIAL_PORT, gson)
    }

    @Provides @Named(DEVICE_COMMAND_SERIAL_PORT)
    fun provideDeviceCommandSerialPort(io: Serial, mqtt: MQTT, gson: Gson) : SerialManager {
        return DeviceSerialPortManager(io, mqtt, DEVICE_COMMAND_SERIAL_PORT, gson)
    }

    @Provides
    fun provideConsole() : Console {
        return Console()
    }

    @Provides
    fun provideIO() : Serial {

        return SerialFactory.createInstance()

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
        private const val SENSOR_SERIAL_PORT = "ttyUSB0"
        private const val DEVICE_COMMAND_SERIAL_PORT = "ttyUSB1"
    }

}