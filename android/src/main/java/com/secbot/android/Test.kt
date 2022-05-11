package com.secbot.android

import com.google.gson.Gson

object Test {
    private val gson = Gson()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello, world!")
        println(gson.toJson(Payload("HB", "PING")))

    }

    val s : String = "{\"topic\":\"HB\",\"message\":\"PING\"}"

    data class Payload(val topic: String, val message: String)


}