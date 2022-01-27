import sys

sys.path.append('../')
import json
import motors.stepper as steering
import motors.motor as motor

def process_command(topic, payload):
    # print(preprocessing command {topic} {payload}')
    command = json.loads(payload)
    if topic == "MotorCommand":
        steering.steer(command)
        motor.move(command)

    return
