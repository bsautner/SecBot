import time

import RPi.GPIO as GPIO
import board
import busio
import digitalio
import py.pi.device.motor.motor as motor
from adafruit_mcp230xx.mcp23008 import MCP23008

#
# clutch
i2c = busio.I2C(board.SCL, board.SDA)
mcp = MCP23008(i2c)
time.sleep(1)

pin_motor_4_a = 20  # GPOIO38
pin_motor_4_b = 21  # GPIO40

pin_motor_3_a = 16  # GPIO36
pin_motor_3_b = 12  # GPIO32

pin_motor_1_a = 23  # GPIO12
pin_motor_1_b = 18  # GPIO16

pin_motor_2_a = 25  # GPIO18
pin_motor_2_b = 24  # GPIO25

clutch_pin_a = mcp.get_pin(1)
clutch_pin_b = mcp.get_pin(2)
clutch_pin_c = mcp.get_pin(3)
clutch_pin_d = mcp.get_pin(4)

clutch_pin_a.switch_to_output(value=False)  # wheel 2
clutch_pin_b.switch_to_output(value=False)  # wheel 3
clutch_pin_c.switch_to_output(value=False)  # wheel 4
clutch_pin_d.switch_to_output(value=False)  # wheel 1

clutches = [clutch_pin_d, clutch_pin_a, clutch_pin_b, clutch_pin_c]

# Init Motors
GPIO.setmode(GPIO.BCM)

GPIO.setup(pin_motor_1_a, GPIO.OUT)
GPIO.setup(pin_motor_1_b, GPIO.OUT)
motor_1_f = GPIO.PWM(pin_motor_1_a, 1000)
motor_1_r = GPIO.PWM(pin_motor_1_b, 1000)
motor_1_f.start(0)
motor_1_r.start(0)
motor_a = [motor_1_f, motor_1_r]

GPIO.setup(pin_motor_2_a, GPIO.OUT)
GPIO.setup(pin_motor_2_b, GPIO.OUT)
motor_2_f = GPIO.PWM(pin_motor_2_a, 1000)
motor_2_r = GPIO.PWM(pin_motor_2_b, 1000)
motor_2_f.start(0)
motor_2_r.start(0)
motor_b = [motor_2_f, motor_2_r]

GPIO.setup(pin_motor_3_a, GPIO.OUT)
GPIO.setup(pin_motor_3_b, GPIO.OUT)
motor_3_f = GPIO.PWM(pin_motor_3_a, 1000)
motor_3_r = GPIO.PWM(pin_motor_3_b, 1000)
motor_3_f.start(0)
motor_3_r.start(0)
motor_c = [motor_3_f, motor_3_r]

GPIO.setup(pin_motor_4_a, GPIO.OUT)
GPIO.setup(pin_motor_4_b, GPIO.OUT)
motor_4_f = GPIO.PWM(pin_motor_4_a, 1000)
motor_4_r = GPIO.PWM(pin_motor_4_b, 1000)
motor_4_f.start(0)
motor_4_r.start(0)
motor_d = [motor_4_f, motor_4_r]

motors = [motor_a, motor_b, motor_c, motor_d]

motor_max = 100
clutch_throttle = 0.2
GPIO.setwarnings(False)


def clutch(n):
    print("Clutch Off:", n)

    clutches[n - 1].switch_to_output(value=True)


def clutchOff(n):
    print("Clutch On:", n)
    clutches[n - 1].switch_to_output(value=False)


def engageClutches():
    print("Clutch On")


def disengageClutches():
    print("Clutch Off")


def driveMotor(m, direction):

    r = 0 if direction == 1 else 1
    motors[m - 1][direction].ChangeDutyCycle(motor_max)
    motors[m - 1][r].ChangeDutyCycle(0)


def run():
    i = 2


    #
    # t = 10
    # driveMotor(i, 0)
    # time.sleep(t)
    # driveMotor(i, 1)
    # time.sleep(t)
    # stopMotor(i)
    # clutchOff(i)

    # brakeStop()
    # time.sleep(4)
    # disengageClutches()
    # time.sleep(0.5)
    # driveMotor(1, 0)
    # time.sleep(5)
    # brakeStop()
    # disengageClutches()
    # fullDrive(1)
    while True:
        time.sleep(1)
    #     for i in range(1, 5):
    #         driveMotor(i, 0)
    #         time.sleep(3)
    #         # disengageClutches()
    #         # time.sleep(3)
    #         driveMotor(i, 1)
    #         # driveMotor(1, 1)
    #         time.sleep(3)
    #         brakeStop()
    #         time.sleep(1)
    #         disengageClutches()
    #         time.sleep(3)


if __name__ == '__main__':
    run()
