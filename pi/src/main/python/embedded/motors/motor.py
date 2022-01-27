import sys

sys.path.append('../')
import time
# import board

import RPi.GPIO as GPIO

pin_motor_1_a = 36
pin_motor_1_b = 38
#
#
GPIO.setup(pin_motor_1_a, GPIO.OUT)
GPIO.setup(pin_motor_1_b, GPIO.OUT)

p1 = GPIO.PWM(pin_motor_1_a, 50)  # channel=12 frequency=50Hz
p1.start(0)

p2 = GPIO.PWM(pin_motor_1_b, 50)  # channel=12 frequency=50Hz
p2.start(0)


def f():
    print("forward")
    p1.ChangeDutyCycle(15)
    p2.ChangeDutyCycle(0)


def r():
    print("reverse")
    p1.ChangeDutyCycle(0)
    p2.ChangeDutyCycle(15)


def stop():
    print("stopping")
    p1.ChangeDutyCycle(0)
    p2.ChangeDutyCycle(0)

def move(command):
    if command == "FORWARD":
        f()
    elif command == "REVERSE":
        r()
    elif command == "STOP":
        stop()

def run():
    print("Testing Motor")
    f()
    time.sleep(3)
    stop()
    time.sleep(3)
    r()
    time.sleep(3)
    stop()
    GPIO.cleanup()
    print("Done Testing Motor")


if __name__ == '__main__':
    run()
