import sys
sys.path.append('../')
import json
import embedded.motors.stepper as steering

def processCommand(topic, payload):
# print(f'processing command {topic} {payload}')
    command = json.loads(payload)
    if topic == "MotorCommand":
          steering.steer(command)


    return