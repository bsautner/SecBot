import time
import threading
import random
import paho.mqtt.client as mqtt_client

import RPi.GPIO as GPIO
import python.pi.device.led.led as led
import python.pi.network.mqtt as mqtt
import python.pi.device.motor.motor as motor
import python.pi.system.command_processor as command_processor
import board
import busio
import digitalio
import adafruit_hcsr04
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
led_1 = mcp.get_pin(6)

pin_motor_2_a = 20  # GPOIO38
pin_motor_2_b = 21  # GPIO40

pin_motor_4_a = 16  # GPIO36
pin_motor_4_b = 12  # GPIO32

pin_motor_1_a = 23  # GPIO12
pin_motor_1_b = 18  # GPIO16

pin_motor_3_a = 24  # GPIO18
pin_motor_3_b = 25  # GPIO25

broker = "localhost"
port = 1883
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'
clutch_throttle = 0.2
max = 100

print("Hello World!!")
led_1.switch_to_output(value=True)

clutch_1.switch_to_output(value=False)
clutch_2.switch_to_output(value=False)
clutch_3.switch_to_output(value=False)
clutch_4.switch_to_output(value=False)

GPIO.setmode(GPIO.BCM)

GPIO.setup(pin_motor_1_a, GPIO.OUT)
GPIO.setup(pin_motor_1_b, GPIO.OUT)
motor_1_f = GPIO.PWM(pin_motor_1_a, 50)
motor_1_r = GPIO.PWM(pin_motor_1_b, 50)
motor_1_f.start(0)
motor_1_r.start(0)

GPIO.setup(pin_motor_2_a, GPIO.OUT)
GPIO.setup(pin_motor_2_b, GPIO.OUT)
motor_2_f = GPIO.PWM(pin_motor_2_a, 50)
motor_2_r = GPIO.PWM(pin_motor_2_b, 50)
motor_2_f.start(0)
motor_2_r.start(0)

GPIO.setup(pin_motor_3_a, GPIO.OUT)
GPIO.setup(pin_motor_3_b, GPIO.OUT)
motor_3_f = GPIO.PWM(pin_motor_3_a, 50)
motor_3_r = GPIO.PWM(pin_motor_3_b, 50)
motor_3_f.start(0)
motor_3_r.start(0)

GPIO.setup(pin_motor_4_a, GPIO.OUT)
GPIO.setup(pin_motor_4_b, GPIO.OUT)
motor_4_f = GPIO.PWM(pin_motor_4_a, 60)
motor_4_r = GPIO.PWM(pin_motor_4_b, 60)
motor_4_f.start(0)
motor_4_r.start(0)


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")

        else:
            print("Failed to connect to mqtt, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, 30)
    return client

    # m = motor.Motor(motor_1_f, motor_1_r)
    # p = command_processor.CommandProcessor(m)  # Command Processor

    # mqtt_client = mqtt.MQTT(p, connect_mqtt())  # MQTT
    #
    # mqtt_process = threading.Thread(target=mqtt_client.run())
    # mqtt_process.setDaemon(True)
    # mqtt_process.start()

    # m.stop()


def run():
    disengageClutches()
    # print("testing motors")
    spin(0)

    time.sleep(3)

    spin(1)

    time.sleep(3)

    fullDrive(0)
    time.sleep(1)
    fullDrive(1)
    time.sleep(1)
    wideTurn(0)
    brakeStop()
    sleep(2)
    wideTurn(1)
    brakeStop()
    sleep(2)
    wideTurn(2)
    brakeStop()
    sleep(2)
    wideTurn(3)
    brakeStop()
    sleep(2)
    wideTurn(4)
    brakeStop()



def sleep(t):
    time.sleep(t)


def driveMotor(n, d):
    if n == 1:
        clutch_1.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_1_f.ChangeDutyCycle(max)
            motor_1_r.ChangeDutyCycle(0)
        else:
            motor_1_f.ChangeDutyCycle(0)
            motor_1_r.ChangeDutyCycle(max)
    elif n == 2:
        clutch_2.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_2_f.ChangeDutyCycle(max)
            motor_2_r.ChangeDutyCycle(0)
        else:
            motor_2_f.ChangeDutyCycle(0)
            motor_2_r.ChangeDutyCycle(max)
    elif n == 3:
        clutch_3.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_3_f.ChangeDutyCycle(max)
            motor_3_r.ChangeDutyCycle(0)
        else:
            motor_3_f.ChangeDutyCycle(0)
            motor_3_r.ChangeDutyCycle(max)
    elif n == 4:
        clutch_4.value = True
        time.sleep(clutch_throttle)
        if d == 0:
            motor_4_f.ChangeDutyCycle(max)
            motor_4_r.ChangeDutyCycle(0)
        else:
            motor_4_f.ChangeDutyCycle(0)
            motor_4_r.ChangeDutyCycle(max)


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


def fullDrive(d):
    engageClutches()
    if d == 0:
        motor_1_f.ChangeDutyCycle(max)
        motor_1_r.ChangeDutyCycle(0)
        motor_2_f.ChangeDutyCycle(max)
        motor_2_r.ChangeDutyCycle(0)
        motor_3_f.ChangeDutyCycle(max)
        motor_3_r.ChangeDutyCycle(0)
        motor_4_f.ChangeDutyCycle(max)
        motor_4_r.ChangeDutyCycle(0)
    else:
        motor_1_f.ChangeDutyCycle(0)
        motor_1_r.ChangeDutyCycle(max)
        motor_2_f.ChangeDutyCycle(0)
        motor_2_r.ChangeDutyCycle(max)
        motor_3_f.ChangeDutyCycle(0)
        motor_3_r.ChangeDutyCycle(max)
        motor_4_f.ChangeDutyCycle(0)
        motor_4_r.ChangeDutyCycle(max)


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


def engageClutches():
    print("Clutch On")
    clutch_1.value = True
    time.sleep(clutch_throttle)
    clutch_2.value = True
    time.sleep(clutch_throttle)
    clutch_3.value = True
    time.sleep(clutch_throttle)
    clutch_4.value = True
    time.sleep(clutch_throttle)
    led_1.value = True


def disengageClutches():
    print("Clutch On")
    clutch_1.value = False
    time.sleep(clutch_throttle)
    clutch_2.value = False
    time.sleep(clutch_throttle)
    clutch_3.value = False
    time.sleep(clutch_throttle)
    clutch_4.value = False
    time.sleep(clutch_throttle)
    led_1.value = False


if __name__ == '__main__':
    run()
