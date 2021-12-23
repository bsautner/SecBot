package com.secbot.pi.devices.audio

import com.secbot.core.AbstractDevice
import java.io.File

object Speaker : AbstractDevice<File>() {

    override fun update(payload: File) {
        //  Runtime.getRuntime().exec("mpg123 /home/pi/speech/online.mp3")
    }
}