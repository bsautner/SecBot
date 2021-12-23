package com.secbot.core


interface Device {


    fun topic() : String {
        return this::class.java.name
    }

    fun name() : String {
        return this::class.java.simpleName
    }

    fun start()
    fun stop()


}