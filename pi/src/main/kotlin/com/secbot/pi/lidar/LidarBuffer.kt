package com.secbot.pi.lidar

object LidarBuffer {


    private val data = HashMap<Int, Int>()
    private val changed = HashMap<Int, Int>()
    private var timestamp = 0L

    fun update(angle: Int, distance: Int): Map<Int, Int> {
        if (distance < 100) {
        if (!data.containsKey(angle)) {
            data[angle] = distance
            changed[angle] = distance
        } else if (data[angle] != distance) {
            data[angle] = distance
            changed[angle] = distance
        }

        return if (System.currentTimeMillis() - timestamp > 500 && changed.isNotEmpty()) {

            val copy = changed.toMap()
            changed.clear()
            timestamp = System.currentTimeMillis()
            copy

        } else {
            emptyMap()
        }
        } else {
            return emptyMap()
        }


    }

}