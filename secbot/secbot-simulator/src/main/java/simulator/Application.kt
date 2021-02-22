package simulator

import com.secbot.control.State

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val state = State.DRIVING_FORWARD

            println("Hello Simulator $state")
        }
    }
}