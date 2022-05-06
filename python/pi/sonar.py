# SPDX-FileCopyrightText: 2021 ladyada for Adafruit Industries
# SPDX-License-Identifier: MIT

import time
import board
import adafruit_hcsr04
import RPi.GPIO as GPIO
# 3:
# T: 22
# E: 13
# 4
# T: 04
# E: 11
# 5
# T: 18
# E: 05
# 6:
# T: 27
# E: 6
# 7:
# T: 10
# E: 19
# 8:
# T: 09
# E: 26



GPIO_TRIGGER = 22
GPIO_ECHO = 13

# Use BCM GPIO references
# instead of physical pin numbers
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)

sonar_1 = adafruit_hcsr04.HCSR04(trigger_pin=board.D14, echo_pin=board.D15)  # ok fast-forward
sonar_2 = adafruit_hcsr04.HCSR04(trigger_pin=board.D8, echo_pin=board.D0)    # ok
sonar_3 = adafruit_hcsr04.HCSR04(trigger_pin=board.D22, echo_pin=board.D13)  # ok pin 15 / pin 33
sonar_4 = adafruit_hcsr04.HCSR04(trigger_pin=board.D4, echo_pin=board.D11)   # ok
sonar_5 = adafruit_hcsr04.HCSR04(trigger_pin=board.D27, echo_pin=board.D5)   # ok fast-forward
sonar_6 = adafruit_hcsr04.HCSR04(trigger_pin=board.D27, echo_pin=board.D6)   # ok
sonar_7 = adafruit_hcsr04.HCSR04(trigger_pin=board.D10, echo_pin=board.D19)
sonar_8 = adafruit_hcsr04.HCSR04(trigger_pin=board.D9, echo_pin=board.D26)   # T: 21 E: 37
time.sleep(1)
delay = 0.5
try:
    print((sonar_3.distance,))
except RuntimeError:
    print("Retrying 3")

while True:
    try:
        print((sonar_1.distance,))
    except RuntimeError:
        print("Retrying 1")
    time.sleep(delay)
    try:
        print((sonar_2.distance,))
    except RuntimeError:
        print("Retrying 2")
    time.sleep(delay)
    try:
        print((sonar_3.distance,))
    except RuntimeError:
        print("Retrying 3")
    time.sleep(delay)
    try:
        print((sonar_4.distance,))
    except RuntimeError:
        print("Retrying 4")
    time.sleep(delay)
    try:
        print((sonar_5.distance,))
    except RuntimeError:
        print("Retrying 5")
    time.sleep(delay)
    try:
        print((sonar_6.distance,))
    except RuntimeError:
        print("Retrying 6")
    time.sleep(delay)
    try:
        print((sonar_7.distance,))
    except RuntimeError:
        print("Retrying 7")
    time.sleep(delay)
    try:
        print((sonar_8.distance,))
    except RuntimeError:
        print("Retrying 8")
    time.sleep(delay)
