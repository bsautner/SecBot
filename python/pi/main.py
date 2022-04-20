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

# pin0 = mcp.get_pin(0)
clutch3 = mcp.get_pin(1)
clutch4 = mcp.get_pin(2)
front_clutch = mcp.get_pin(3)

pin_motor_1_a = 20  # GPOIO38
pin_motor_1_b = 21  # GPIO40

pin_motor_2_a = 23  # GPIO12
pin_motor_2_b = 18  # GPIO16

pin_motor_3_a = 16  # GPIO18
pin_motor_3_b = 12  # GPIO23

pin_motor_4_a = 24  # GPIO18
pin_motor_4_b = 25  # GPIO25

broker = "localhost"
port = 1883
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'


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


def run():
    print("Hello World!!")
    # led.blink(1)

    # pin0.switch_to_output(value=False)
    clutch3.switch_to_output(value=False)
    clutch4.switch_to_output(value=False)
    front_clutch.switch_to_output(value=False)

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

    # m = motor.Motor(motor_1_f, motor_1_r)
    # p = command_processor.CommandProcessor(m)  # Command Processor

    # mqtt_client = mqtt.MQTT(p, connect_mqtt())  # MQTT
    #
    # mqtt_process = threading.Thread(target=mqtt_client.run())
    # mqtt_process.setDaemon(True)
    # mqtt_process.start()

    # m.stop()

    max = 100

    while True:
        print("testing motors")

        print("Clutch On")

        front_clutch.value = True
        time.sleep(0.01)
        clutch3.value = False   # Front Clutch
        time.sleep(0.01)
        clutch4.value = False

        motor_1_f.ChangeDutyCycle(0)
        motor_1_r.ChangeDutyCycle(max)

        motor_2_f.ChangeDutyCycle(0)
        motor_2_r.ChangeDutyCycle(max)

        motor_3_f.ChangeDutyCycle(0)
        motor_3_r.ChangeDutyCycle(max)

        motor_4_f.ChangeDutyCycle(0)
        motor_4_r.ChangeDutyCycle(max)
        print("Motors On Reverse")

        time.sleep(3)
        time.sleep(3)
        print("clutch off")
        # pin0.value = False

        front_clutch.value = False
        time.sleep(0.01)
        clutch3.value = False   # Front Clutch
        time.sleep(0.01)
        clutch4.value = False

        motor_1_f.ChangeDutyCycle(0)
        motor_1_r.ChangeDutyCycle(0)

        motor_2_f.ChangeDutyCycle(0)
        motor_2_r.ChangeDutyCycle(0)

        motor_3_f.ChangeDutyCycle(0)
        motor_3_r.ChangeDutyCycle(0)

        motor_4_f.ChangeDutyCycle(0)
        motor_4_r.ChangeDutyCycle(0)
        print("Motors Off")
        time.sleep(3)

        print("Clutch On")
        # pin0.value = True
        time.sleep(0.01)
        clutch3.value = False
        time.sleep(0.01)
        clutch4.value = False
        time.sleep(0.01)
        front_clutch.value = True

        print("Motor 1")
        motor_1_f.ChangeDutyCycle(max)
        motor_1_r.ChangeDutyCycle(0)

        print("Motor 2")
        motor_2_f.ChangeDutyCycle(max)
        motor_2_r.ChangeDutyCycle(0)

        print("Motor 3")
        motor_3_f.ChangeDutyCycle(max)
        motor_3_r.ChangeDutyCycle(0)

        print("Motor 4")
        motor_4_f.ChangeDutyCycle(max)
        motor_4_r.ChangeDutyCycle(0)
        print("Motors On Forward")
        time.sleep(3)


if __name__ == '__main__':
    run()
