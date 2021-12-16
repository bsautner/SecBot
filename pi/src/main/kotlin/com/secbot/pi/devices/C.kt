package com.secbot.pi.devices

import com.pi4j.util.Console

object C {

    private var console = Console()

    fun print(data: Any?) {
        console.println(data)
     }

    fun promptForExit() {
        console.promptForExit()
    }

    fun box(s: String) {
        console.box(s)
    }


}