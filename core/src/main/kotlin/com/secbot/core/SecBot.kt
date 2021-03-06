package com.secbot.core

import com.secbot.core.hardware.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
class SecBot  {

    var state: State = State.POWER_ON

//    override fun send(command: SensorData) {
//
////        if (serial.isOpen) {
////            serial.write(command.toSerialCommand())
////        } else {
////            println("Can't send serial command because port is closed")
////        }
//    }

//     fun receiveSensorData(data: SensorData) {
//        TODO("Not yet implemented")
//    }
//    SerialData(device=SCANNING_SONAR, v=104.0, timestamp=1614953950281, value=104.0)
//    SerialData(device=SCANNING_IR, v=77.0, timestamp=1614953950282, value=77.0)

//    private val devices = HashMap<Control, SerialData>().apply {
//        this[Control.ACCELEROMETER_X] = SerialData(Control.ACCELEROMETER_X, 0.0, System.currentTimeMillis())
//        this[Control.ACCELEROMETER_Y] = SerialData(Control.ACCELEROMETER_Y, 0.0, System.currentTimeMillis())
//        this[Control.ACCELEROMETER_Z] = SerialData(Control.ACCELEROMETER_Z, 0.0, System.currentTimeMillis())
//        this[Control.COMPASS_HEADING] = SerialData(Control.COMPASS_HEADING, 0.0, System.currentTimeMillis())
//        this[Control.FRONT_SONAR] = SerialData(Control.FRONT_SONAR, 0.0, System.currentTimeMillis())
//        this[Control.MOTOR_1] = SerialData(Control.MOTOR_1, 0.0, System.currentTimeMillis())
//        this[Control.MOTOR_2] = SerialData(Control.MOTOR_2, 0.0, System.currentTimeMillis())
//        this[Control.STEERING_SERVO] = SerialData(Control.STEERING_SERVO, 45.0, System.currentTimeMillis())
//        this[Control.SCANNING_SONAR] = SerialData(Control.SCANNING_SONAR, 0.0, System.currentTimeMillis())
//        this[Control.SCANNING_IR] = SerialData(Control.SCANNING_IR, 0.0, System.currentTimeMillis())
//
//    }

//    val orientation: Int
//        get() {
//            return devices[Control.COMPASS_HEADING]?.value?.toInt() ?: 0
//        }
//
//    val forwardSonarDistanceCm: Double
//        get() {
//            return devices[Control.FRONT_SONAR]?.value ?: 0.0
//        }
//
//    val scanningSonarDistanceCm: Double
//        get() {
//            return devices[Control.SCANNING_SONAR]?.value ?: 0.0
//        }
//
//    val scanningIRDistanceCm: Double
//        get() {
//            return devices[Control.SCANNING_IR]?.value ?: 0.0
//        }
//
//    val accel: String
//        get() {
//            return "x: ${devices[Control.ACCELEROMETER_X]?.value} y: ${devices[Control.ACCELEROMETER_Y]?.value} z: ${devices[Control.ACCELEROMETER_Z]?.value}"
//        }
//
//    val forwardSonarNotChanging: Boolean
//        get() {
//            return System.currentTimeMillis() - (devices[Control.FRONT_SONAR]?.timestamp ?: 0) > 1000
//        }
//
//    val forwardClear: Boolean
//        get() {
//            return forwardSonarDistanceCm > MIN_FRONT_SPACE_CM
//        }

//    val speed: Double
//        get() {
//            val speed = when {
//                state == State.BACKING_UP -> {
//                    -15
//                }
//                forwardSonarDistanceCm > 500 -> {
//                    50
//                }
//                forwardSonarDistanceCm.toInt() in 11..99 -> {
//                    15
//                }
//                forwardSonarDistanceCm < 10 && state != State.BACKING_UP -> {
//                    0
//                }
//
//                else -> {
//                    25
//                }
//            }
//            //TODO remove flip when wires to motor can be reversed
//         //   devices[Control.MOTOR_1]?.value = speed.toDouble() * -1
//            return speed.toDouble() * -1
//        }
//
//
//    private fun changeState(newState: State) {
//        println("State Changed from $state to $newState at speed $speed with [f: $forwardSonarDistanceCm / s: $scanningSonarDistanceCm / ir: $scanningIRDistanceCm] cm of free space heading $orientation")
//        state = newState
//    }
//
//    override fun send(command: SerialData) {
//        devices[command.device] = command
//    }
//
//    override fun receive(data: SerialData) {
//        devices[data.device] = data
//    }

     fun receiver(scope: CoroutineScope, data: ReceiveChannel<Device>): ReceiveChannel<Device> =
        scope.produce {

            for (s in data) {
                //  println("SecBot Receiver got command: $s")
                send(s)
            }
        }


     fun start(scope: CoroutineScope): ReceiveChannel<Device> = scope.produce {
        delay(1500)
        println("calibrating")

//        stop()
//        val cmd = DeviceCommand(Control.STEERING_SERVO, 70.0)
//        send(cmd)
//        delay(500)
//        send(DeviceCommand(Control.STEERING_SERVO, 45.0))
//        delay(500)
//        send(DeviceCommand(Control.STEERING_SERVO, 20.0))
//        delay(500)
//        send(DeviceCommand(Control.STEERING_SERVO, 45.0))
//        delay(500)
//
//        send(DeviceCommand(Control.SCANNING_SERVO, 160.0))
//        delay(500)
//        send(DeviceCommand(Control.SCANNING_SERVO, 20.0))
//        delay(500)
//        send(DeviceCommand(Control.SCANNING_SERVO, 160.0))
//        delay(500)
//        send(DeviceCommand(Control.SCANNING_SERVO, 90.0))
//        delay(500)

        while (true) {
          //  println("$state at speed $speed with [f: $forwardSonarDistanceCm / s: $scanningSonarDistanceCm / ir: $scanningIRDistanceCm]  cm of free space heading $orientation accellerating $accel")

            when {
//                (state == State.BACKING_UP && forwardSonarNotChanging && !forwardClear) -> {  //i'm stuck
//                    changeState(State.STUCK_BACKWARD)
//                    stop()
//                }
//
//                (state == State.DRIVING_FORWARD && forwardSonarNotChanging && forwardClear) -> {  //i'm stuck
//                    changeState(State.STUCK_FORWARD)
//                    stop()
//                }
//
//                (state == State.STUCK_FORWARD) -> {
//                    findNewDirection(this, this@SecBot)
//                }
//
//                (state == State.STUCK_BACKWARD) -> {
//                    findNewDirection(this, this@SecBot)
//                }
//
//
//                (forwardClear && state != State.LOOKING && state != State.BACKING_UP && state != State.DRIVING_FORWARD && state != State.STUCK_FORWARD && state != State.STUCK_BACKWARD) -> {
//                    driveForward()
//
//                }
//                (state == State.DRIVING_FORWARD && forwardSonarDistanceCm > 10) -> {
//                    send(SerialData(Control.MOTOR_1, speed, System.currentTimeMillis()))
//                }
//                (!forwardClear && state != State.STOPPED && state != State.LOOKING && state != State.BACKING_UP) -> {
//                    send(SerialData(Control.MOTOR_1, speed, System.currentTimeMillis()))
//                    changeState(State.STOPPED)
//                }
//                (state == State.STOPPED && state != State.STUCK_BACKWARD && !forwardClear) -> {
//
//                    changeState(State.BACKING_UP)
//                    send(SerialData(Control.MOTOR_1, speed, System.currentTimeMillis()))
//                }
//                (!forwardClear && state == State.BACKING_UP) -> {
//                    send(SerialData(Control.MOTOR_1, speed, System.currentTimeMillis()))
//                    wait()
//                    changeState(State.LOOKING)
//                }
//                (state == State.BACKING_UP && forwardClear) -> {
//                    changeState(State.DRIVING_FORWARD)
//                }
//
//
//                (state == State.LOOKING) -> {
//                    findNewDirection(this, this@SecBot)
//                    if (forwardSonarDistanceCm >= MIN_FRONT_SPACE_CM) {
//                        changeState(State.DRIVING_FORWARD)
//                    }
//                }


            }
            // send(SerialData(Device.PING, 0.0, System.currentTimeMillis()))
            delay(500)

        }


    }

    private suspend fun ProducerScope<Device>.driveForward() {
     //   changeState(State.DRIVING_FORWARD)
       // send(SerialData(Control.MOTOR_1, speed, System.currentTimeMillis()))

    }


//
//    private suspend fun ProducerScope<SensorData>.backwardsSlow() {
//        send(SensorData(Control.MOTOR_1, -20.0, System.currentTimeMillis()))
//    }
//
//    private suspend fun ProducerScope<SensorData>.forwardSlow() {
//        send(SensorData(Control.MOTOR_1, 15.0, System.currentTimeMillis()))
//    }
//
//    private suspend fun wait() {
//        delay(1000)
//    }

//    private suspend fun ProducerScope<SensorData>.stop() {
//        send(DeviceCommand(Control.MOTOR_1, 0.0))
//    }


    companion object {

        private const val MIN_FRONT_SPACE_CM = 10
    }


}