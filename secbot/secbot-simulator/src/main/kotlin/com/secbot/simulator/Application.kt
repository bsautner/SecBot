package com.secbot.simulator

import com.secbot.control.State

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val state = State.CLEAR

            println("Hello Simulator $state")
        }
    }
}