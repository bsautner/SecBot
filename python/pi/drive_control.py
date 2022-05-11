import time

import RPi.GPIO as GPIO
import board
import busio
import digitalio
import python.pi.device.motor.motor as motor
from adafruit_mcp230xx.mcp23008 import MCP23008


#
# clutch
i2c = busio.I2C(board.SCL, board.SDA)
mcp = MCP23008(i2c)
time.sleep(1)

clutch_1 = mcp.get_pin(4)
clutch_2 = mcp.get_pin(3)
clutch_3 = mcp.get_pin(2)
clutch_4 = mcp.get_pin(1)


pin_motor_2_a = 20  # GPOIO38
pin_motor_2_b = 21  # GPIO40

pin_motor_4_a = 16  # GPIO36
pin_motor_4_b = 12  # GPIO32

pin_motor_1_a = 23  # GPIO12
pin_motor_1_b = 18  # GPIO16

pin_motor_3_a = 24  # GPIO18
pin_motor_3_b = 25  # GPIO25

clutch_pin_1 = mcp.get_pin(4)
clutch_pin_2 = mcp.get_pin(3)
clutch_pin_3 = mcp.get_pin(2)
clutch_pin_4 = mcp.get_pin(1)

clutch_1.switch_to_output(value=False)
clutch_2.switch_to_output(value=False)
clutch_3.switch_to_output(value=False)
clutch_4.switch_to_output(value=False)


# Init Motors
GPIO.setmode(GPIO.BCM)

GPIO.setup(pin_motor_1_a, GPIO.OUT)
GPIO.setup(pin_motor_1_b, GPIO.OUT)
motor_1_f = GPIO.PWM(pin_motor_1_a, 1000)
motor_1_r = GPIO.PWM(pin_motor_1_b, 1000)
motor_1_f.start(0)
motor_1_r.start(0)

GPIO.setup(pin_motor_2_a, GPIO.OUT)
GPIO.setup(pin_motor_2_b, GPIO.OUT)
motor_2_f = GPIO.PWM(pin_motor_2_a, 1000)
motor_2_r = GPIO.PWM(pin_motor_2_b, 1000)


GPIO.setup(pin_motor_3_a, GPIO.OUT)
GPIO.setup(pin_motor_3_b, GPIO.OUT)
motor_3_f = GPIO.PWM(pin_motor_3_a, 1000)
motor_3_r = GPIO.PWM(pin_motor_3_b, 1000)


GPIO.setup(pin_motor_4_a, GPIO.OUT)
GPIO.setup(pin_motor_4_b, GPIO.OUT)
motor_4_f = GPIO.PWM(pin_motor_4_a, 1000)
motor_4_r = GPIO.PWM(pin_motor_4_b, 1000)


motor_max = 100
clutch_throttle = 0.2
GPIO.setwarnings(False)

def fullDrive(d):
    engageClutches()

    if d == 1:
        motor_1_f.ChangeDutyCycle(motor_max)
        motor_1_r.ChangeDutyCycle(0)
        motor_2_f.ChangeDutyCycle(motor_max)
        motor_2_r.ChangeDutyCycle(0)
        motor_3_f.ChangeDutyCycle(motor_max)
        motor_3_r.ChangeDutyCycle(0)
        motor_4_f.ChangeDutyCycle(motor_max)
        motor_4_r.ChangeDutyCycle(0)
    else:
        motor_1_f.ChangeDutyCycle(0)
        motor_1_r.ChangeDutyCycle(motor_max)
        motor_2_f.ChangeDutyCycle(0)
        motor_2_r.ChangeDutyCycle(motor_max)
        motor_3_f.ChangeDutyCycle(0)
        motor_3_r.ChangeDutyCycle(motor_max)
        motor_4_f.ChangeDutyCycle(0)
        motor_4_r.ChangeDutyCycle(motor_max)


def brakeStop():

    engageClutches()
    motor_1_f.ChangeDutyCycle(0)
    motor_1_r.ChangeDutyCycle(0)
    motor_2_f.ChangeDutyCycle(0)
    motor_2_r.ChangeDutyCycle(0)

    motor_3_f.ChangeDutyCycle(0)
    motor_3_r.ChangeDutyCycle(0)

    motor_4_f.ChangeDutyCycle(0)
    motor_4_r.ChangeDutyCycle(0)


def engageClutches():
    print("Clutch On")
    clutch_pin_1.value = True
    time.sleep(clutch_throttle)
    clutch_pin_2.value = True
    time.sleep(clutch_throttle)
    clutch_pin_3.value = True
    time.sleep(clutch_throttle)
    clutch_pin_4.value = True
    time.sleep(clutch_throttle)


def disengageClutches():
    print("Clutch Off")
    clutch_pin_1.value = False
    time.sleep(clutch_throttle)
    clutch_pin_2.value = False
    time.sleep(clutch_throttle)
    clutch_pin_3.value = False
    time.sleep(clutch_throttle)
    clutch_pin_4.value = False
    time.sleep(clutch_throttle)


def wideTurn(d):
    disengageClutches()

    if d == 1:
        clutch_3.value = False
        sleep(clutch_throttle)
        clutch_2.value = False
        sleep(clutch_throttle)
        driveMotor(1, 0)
        driveMotor(4, 0)

    elif d == 2:
        clutch_1.value = False
        sleep(clutch_throttle)
        clutch_4.value = False
        sleep(clutch_throttle)
        driveMotor(2, 0)
        driveMotor(3, 0)

    elif d == 3:
        clutch_3.value = False
        sleep(clutch_throttle)
        clutch_2.value = False
        sleep(clutch_throttle)
        driveMotor(1, 1)
        driveMotor(4, 1)

    elif d == 4:
        clutch_1.value = False
        sleep(clutch_throttle)
        clutch_4.value = False
        sleep(clutch_throttle)
        driveMotor(2, 1)
        driveMotor(3, 1)


def spin(d):
    if d == 0:
        driveMotor(1, 0)
        driveMotor(4, 0)
        driveMotor(2, 1)
        driveMotor(3, 1)
    else:
        driveMotor(1, 1)
        driveMotor(4, 1)
        driveMotor(2, 0)
        driveMotor(3, 0)


def driveMotor(n, d):
    if n == 1:
        clutch_pin_1.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_1_f.ChangeDutyCycle(motor_max)
            motor_1_r.ChangeDutyCycle(0)
        else:
            motor_1_f.ChangeDutyCycle(0)
            motor_1_r.ChangeDutyCycle(motor_max)
    elif n == 2:
        clutch_pin_2.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_2_f.ChangeDutyCycle(motor_max)
            motor_2_r.ChangeDutyCycle(0)
        else:
            motor_2_f.ChangeDutyCycle(0)
            motor_2_r.ChangeDutyCycle(motor_max)
    elif n == 3:
        clutch_pin_3.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_3_f.ChangeDutyCycle(motor_max)
            motor_3_r.ChangeDutyCycle(0)
        else:
            motor_3_f.ChangeDutyCycle(0)
            motor_3_r.ChangeDutyCycle(motor_max)
    elif n == 4:
        clutch_pin_4.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_4_f.ChangeDutyCycle(motor_max)
            motor_4_r.ChangeDutyCycle(0)
        else:
            motor_4_f.ChangeDutyCycle(0)
            motor_4_r.ChangeDutyCycle(motor_max)




def run():
    brakeStop()
    disengageClutches()
    # fullDrive(1)
    while True:
        time.sleep(1)


if __name__ == '__main__':
    run()
