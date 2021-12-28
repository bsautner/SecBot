package com.secbot.core

import java.util.*


interface Device {


    fun topic() : String {
        return this::class.java.simpleName.toUpperCase(Locale.ROOT)
    }

    fun name() : String {
        return this::class.java.simpleName
    }

    fun start()
    fun stop()


}