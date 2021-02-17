package com.secbot.control

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
class SecBot : IO {

    var state: State = State.IDLE


    private val devices = HashMap<Device, SerialData>().apply {
        this[Device.ACCELEROMETER_X] = SerialData(Device.ACCELEROMETER_X, 0.0, System.currentTimeMillis())
        this[Device.ACCELEROMETER_Y] = SerialData(Device.ACCELEROMETER_Y, 0.0, System.currentTimeMillis())
        this[Device.ACCELEROMETER_Z] = SerialData(Device.ACCELEROMETER_Z, 0.0, System.currentTimeMillis())
        this[Device.COMPASS_HEADING] = SerialData(Device.COMPASS_HEADING, 0.0, System.currentTimeMillis())
        this[Device.FRONT_SONAR] = SerialData(Device.FRONT_SONAR, 0.0, System.currentTimeMillis())
        this[Device.MOTOR_1] = SerialData(Device.MOTOR_1, 0.0, System.currentTimeMillis())
        this[Device.MOTOR_2] = SerialData(Device.MOTOR_2, 0.0, System.currentTimeMillis())
        this[Device.STEERING_SERVO] = SerialData(Device.STEERING_SERVO, 45.0, System.currentTimeMillis())
    }


    val forwardSonarDistanceCm: Double
        get() {
            return devices[Device.FRONT_SONAR]?.value ?: 0.0
        }

    val speed: Double
        get() {
            val speed = when {
                state == State.BACKING_UP -> {
                    -15
                }
                forwardSonarDistanceCm > 500 -> {
                    50
                }
                forwardSonarDistanceCm.toInt() in 11..99 -> {
                    15
                }
                forwardSonarDistanceCm < 10 && state != State.BACKING_UP -> {
                    0
                }

                else -> {
                    25
                }
            }
            //TODO remove flip when wires to motor can be reversed
            devices[Device.MOTOR_1]?.value = speed.toDouble()* -1
            return speed.toDouble() * -1
        }


    private fun changeState(newState: State) {
        println("State Changed from $state to $newState at speed $speed with $forwardSonarDistanceCm cm of free space")
        state = newState
    }

    override fun sendCommand(command: SerialData) {
        devices[command.device] = command
        println(">>> ${command.toSerialCommand()}")


    }

    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

        for (s in data) {
         //   println("SecBot Receiver got command: $s")
            send(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {

      //  println("starting secbot on thread: ${Thread.currentThread().name}")
        while (true) {
            println("State  $state at speed $speed with $forwardSonarDistanceCm cm of free space")

            when {
                (forwardSonarDistanceCm > MIN_FRONT_SPACE_CM && state != State.LOOKING && state != State.BACKING_UP) -> {
                    changeState(State.CLEAR)
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))

                }
                (state == State.CLEAR && forwardSonarDistanceCm > 10) -> {
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                }
                (forwardSonarDistanceCm < MIN_FRONT_SPACE_CM && state != State.STOPPED && state != State.LOOKING && state != State.BACKING_UP) -> {
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                    changeState(State.STOPPED)
                }
                (state == State.STOPPED && forwardSonarDistanceCm < MIN_FRONT_SPACE_CM) -> {

                    changeState(State.BACKING_UP)
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                }
                (forwardSonarDistanceCm <= MIN_FRONT_SPACE_CM && state == State.BACKING_UP) -> {
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                    delay(1000)
                    changeState(State.LOOKING)
                }
                (state == State.LOOKING) -> {
//                    send(SerialData(Device.MOTOR_1, 0.0, System.currentTimeMillis()))
//                    delay(1000)
//                    send(SerialData(Device.STEERING_SERVO, 30.0, System.currentTimeMillis()))
//                    delay(500)
//                    send(SerialData(Device.MOTOR_1, -20.0, System.currentTimeMillis()))
//                    delay(1000)
//                    send(SerialData(Device.STEERING_SERVO, 70.0, System.currentTimeMillis()))
//                    delay(500)
//                    send(SerialData(Device.MOTOR_1, 20.0, System.currentTimeMillis()))
//                    delay(1000)
//                    send(SerialData(Device.STEERING_SERVO, 45.0, System.currentTimeMillis()))
//                    send(SerialData(Device.MOTOR_1, 0.0, System.currentTimeMillis()))
//                    delay(1000)
                    if (forwardSonarDistanceCm >= MIN_FRONT_SPACE_CM) {
                        changeState(State.CLEAR)
                    }
                }


            }
            delay(500)

        }


    }


    companion object {

        private const val MIN_FRONT_SPACE_CM = 10
    }


}