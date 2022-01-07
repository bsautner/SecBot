package com.secbot.core.devices.lidar



class Lidar  {

    val data: MutableMap<Int, LidarPoint> = HashMap()

    init {
        for (i in 1..(36000)) {
            data[i] = LidarPoint()

        }
    }


    fun update(value: LidarPoint): Boolean {

        if (value.index > data.size) {
            throw RuntimeException("wierd angle")
        }
        val old: LidarPoint? = data[value.index ]
        old?.let {
            if (old.distance != value.distance) {
                data[value.index ] = value
                return true
            }

        }
        return false

    }

    companion object {
         val topic :String = Lidar::class.java.simpleName
    }


}

