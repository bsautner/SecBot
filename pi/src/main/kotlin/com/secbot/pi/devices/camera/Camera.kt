package com.secbot.pi.devices.camera

import com.hopding.jrpicam.RPiCamera
import com.hopding.jrpicam.enums.Exposure
import com.secbot.pi.Const
import com.secbot.core.AbstractDevice
import java.io.File
import java.util.*

object Camera : AbstractDevice<Optional<File>>() {

    private var cam: RPiCamera = RPiCamera(Const.PHOTO_PATH)
        .setWidth(300).setHeight(200)
        .setBrightness(40)
        .setExposure(Exposure.AUTO)
        .setContrast(50).setAddRawBayer(false)
        .setQuality(75)
        // .setVerticalFlipOn()
        // .setRegionOfInterest(0.5, 0.5, 0.25, 0.25)
        .setTimeout(1000)

    init {

        cam.setFullPreviewOn()

    }

    private suspend fun takePhoto() {


        val file = "${UUID.randomUUID()}.jpg"
        cam.takeStill(file, 500, 500)
        println("Took a photo of obstruction $file")

        update(Optional.of(File("${Const.PHOTO_PATH}/$file")))


    }


    override fun update(payload: Optional<File>) {
        TODO("Not yet implemented")
    }
}