from adafruit_motor import stepper
import time
import board
from adafruit_motorkit import MotorKit

d = 40
s = 0.01

kit = MotorKit(i2c=board.I2C())

pos = 0


def steer(command):
    global pos

    print(pos)
    if command['motion'] == "LEFT":
        while pos < 40:
            pos = pos + 1
            kit.stepper2.onestep(direction=stepper.FORWARD, style=stepper.DOUBLE)
            time.sleep(s)
    if command['motion'] == "RIGHT":
        while pos > -40:
            pos = pos - 1
            kit.stepper2.onestep(direction=stepper.BACKWARD, style=stepper.DOUBLE)
            time.sleep(s)

    if command['motion'] == "STOP":
        if pos < 0:
            while pos != 0:
                pos = pos + 1
                kit.stepper2.onestep(direction=stepper.FORWARD, style=stepper.DOUBLE)
                time.sleep(s)
        if pos > 0:
            while pos != 0:
                pos = pos - 1
                kit.stepper2.onestep(direction=stepper.BACKWARD, style=stepper.DOUBLE)
                time.sleep(s)
