import json
import device.motor.motor as motor


class CommandProcessor:
    def __init__(self, motor):
        # self.steering = steering.Stepper()
        self.motor = motor

    def process_command(self, topic, payload):
        # print(preprocessing command {topic} {payload}')
        command = json.loads(payload)
        print(command)
        if topic == "MotorCommand":
            print("MotorCommand")
            cmd = json.loads(payload)
            self.motor.move(cmd)
            self.steer.turn(10)
            # steering.steer(cmd)

        return
