# SPDX-FileCopyrightText: 2021 ladyada for Adafruit Industries
# SPDX-License-Identifier: MIT

"""Simple test for using adafruit_motorkit with a stepper motor"""
from adafruit_motor import stepper
import time
import board
from adafruit_motorkit import MotorKit
d = 40
s = 0.01

kit = MotorKit(i2c=board.I2C())
for x in range(10):
    for i in range(d):
        kit.stepper2.onestep(direction=stepper.FORWARD, style=stepper.DOUBLE)
        time.sleep(s)
    time.sleep(4)
    for i in range(d):
        kit.stepper2.onestep(direction=stepper.FORWARD, style=stepper.DOUBLE)
        time.sleep(s)
    time.sleep(4)
    for i in range(d):
        kit.stepper2.onestep(direction=stepper.BACKWARD, style=stepper.DOUBLE)
        time.sleep(s)
    time.sleep(4)
    for i in range(d):
        kit.stepper2.onestep(direction=stepper.BACKWARD, style=stepper.DOUBLE)
        time.sleep(s)
    time.sleep(4)


