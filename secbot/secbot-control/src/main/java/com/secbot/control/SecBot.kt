package com.secbot.control

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
class SecBot : IO {

    var state: State = State.POWER_ON


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

    val orientation : Int
        get() {
            return devices[Device.COMPASS_HEADING]?.value?.toInt() ?: 0
        }

    val forwardSonarDistanceCm: Double
        get() {
            return devices[Device.FRONT_SONAR]?.value ?: 0.0
        }

    val accel : String
        get() {
            return "x: ${devices[Device.ACCELEROMETER_X]?.value} y: ${devices[Device.ACCELEROMETER_Y]?.value} z: ${devices[Device.ACCELEROMETER_Z]?.value}"
        }

    val forwardSonarNotChanging : Boolean
        get() {
            return System.currentTimeMillis() - (devices[Device.FRONT_SONAR]?.timestamp ?: 0) > 1000
        }

    val forwardClear : Boolean
        get() {
            return forwardSonarDistanceCm > MIN_FRONT_SPACE_CM
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
        println("State Changed from $state to $newState at speed $speed with $forwardSonarDistanceCm cm of free space heading $orientation")
        state = newState
    }

    override fun sendCommand(command: SerialData) {
        devices[command.device] = command
    }

    override fun receiver(scope: CoroutineScope, data: ReceiveChannel<SerialData>): ReceiveChannel<SerialData> = scope.produce {

        for (s in data) {
          //  println("SecBot Receiver got command: $s")
            send(s)
        }
    }

    override fun start(scope: CoroutineScope) = scope.produce {



        while (true) {
            println("$state at speed $speed with $forwardSonarDistanceCm cm of free space heading $orientation accellerating $accel")

            when {
                (state == State.BACKING_UP && forwardSonarNotChanging && ! forwardClear) -> {  //i'm stuck
                    changeState(State.STUCK_BACKWARD)
                    stop()



                }

                (state == State.DRIVING_FORWARD && forwardSonarNotChanging &&  forwardClear) -> {  //i'm stuck
                    changeState(State.STUCK_FORWARD)
                    stop()



                }

                (state == State.STUCK_FORWARD) ->  {
                    findNewDirection(this, this@SecBot)
                }

                (state == State.STUCK_BACKWARD) ->  {
                    findNewDirection(this, this@SecBot)
                }


                (forwardClear  && state != State.LOOKING && state != State.BACKING_UP && state != State.DRIVING_FORWARD && state != State.STUCK_FORWARD && state != State.STUCK_BACKWARD) -> {
                    driveForward()

                }
                (state == State.DRIVING_FORWARD && forwardSonarDistanceCm > 10) -> {
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                }
                (! forwardClear && state != State.STOPPED && state != State.LOOKING && state != State.BACKING_UP) -> {
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                    changeState(State.STOPPED)
                }
                (state == State.STOPPED && state != State.STUCK_BACKWARD && ! forwardClear) -> {

                    changeState(State.BACKING_UP)
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                }
                (! forwardClear && state == State.BACKING_UP) -> {
                    send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))
                    wait()
                    changeState(State.LOOKING)
                }
                (state == State.BACKING_UP && forwardClear ) -> {
                    changeState(State.DRIVING_FORWARD)
                }


                (state == State.LOOKING) -> {
                    findNewDirection(this, this@SecBot)
                    if (forwardSonarDistanceCm >= MIN_FRONT_SPACE_CM) {
                        changeState(State.DRIVING_FORWARD)
                    }
                }


            }
           // send(SerialData(Device.PING, 0.0, System.currentTimeMillis()))
            delay(500)

        }



    }

    private suspend fun ProducerScope<SerialData>.driveForward() {
        changeState(State.DRIVING_FORWARD)
        send(SerialData(Device.MOTOR_1, speed, System.currentTimeMillis()))

    }

    private suspend fun findNewDirection(
        producerScope: ProducerScope<SerialData>,
        secBot: SecBot
    ) {
        producerScope.stop()
        secBot.wait()
        producerScope.send(SerialData(Device.STEERING_SERVO, 30.0, System.currentTimeMillis()))
        delay(500)
        producerScope.backwardsSlow()
        secBot.wait()
        producerScope.send(SerialData(Device.STEERING_SERVO, 70.0, System.currentTimeMillis()))
        delay(500)
        producerScope.forwardSlow()
        secBot.wait()
        producerScope.send(SerialData(Device.STEERING_SERVO, 45.0, System.currentTimeMillis()))
        producerScope.stop()
        secBot.wait()
        producerScope.driveForward()
    }

    private suspend fun ProducerScope<SerialData>.backwardsSlow() {
        send(SerialData(Device.MOTOR_1, -20.0, System.currentTimeMillis()))
    }

    private suspend fun ProducerScope<SerialData>.forwardSlow() {
        send(SerialData(Device.MOTOR_1, 15.0, System.currentTimeMillis()))
    }

    private suspend fun wait() {
        delay(1000)
    }

    private suspend fun ProducerScope<SerialData>.stop() {
        send(SerialData(Device.MOTOR_1, 0.0, System.currentTimeMillis()))
    }


    companion object {

        private const val MIN_FRONT_SPACE_CM = 10
    }


}