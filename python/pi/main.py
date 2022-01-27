import time
import threading
import random
import paho.mqtt.client as mqtt_client

import RPi.GPIO as GPIO
import python.pi.device.led.led as led
import python.pi.network.mqtt  # MQTT
import python.pi.device.motor.motor as motor
import python.pi.system.command_processor

pin_motor_1_a = 24
pin_motor_1_b = 23

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
    led.blink(1)

    GPIO.setmode(GPIO.BCM)
    GPIO.setup(pin_motor_1_a, GPIO.OUT)
    GPIO.setup(pin_motor_1_b, GPIO.OUT)
    p1 = GPIO.PWM(pin_motor_1_a, 50)
    p2 = GPIO.PWM(pin_motor_1_b, 50)
    p1.start(0)
    p2.start(0)
    m = motor.Motor(p1, p2)
    p = python.pi.system.command_processor.CommandProcessor(m)  # Command Processor

    mqtt = python.pi.network.mqtt.MQTT(p, connect_mqtt())

    mqtt_process = threading.Thread(target=mqtt.run())
    mqtt_process.setDaemon(True)
    mqtt_process.start()

    m.stop()

    while True:
        time.sleep(1)


if __name__ == '__main__':
    run()
