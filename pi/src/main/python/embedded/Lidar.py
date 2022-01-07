import RPi.GPIO as GPIO
from math import cos, sin, pi, floor
from adafruit_rplidar import RPLidar
from adafruit_rplidar import RPLidarException
import socket
from time import sleep
import time
import board
import digitalio
import json
import random
import paho.mqtt.client as mqtt_client

broker = "localhost"
port = 1883
topic = "RAW_LIDAR"
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'

PORT_NAME = '/dev/ttyUSB0'

# used to scale data to fit on the screen
max_distance = 0
scan_data = [0] * 360
led = digitalio.DigitalInOut(board.D4)
led.direction = digitalio.Direction.OUTPUT


# quality, angle, distance https://github.com/adafruit/Adafruit_CircuitPython_RPLIDAR/blob
# /208b16f92b6491d3d950d587bc9eb60ea2d51251/adafruit_rplidar.py#L508
def collect_data(client):
    try:
        led.value = False
        sleep(2)
        led.value = True
        sleep(2)
        lidar = RPLidar(None, PORT_NAME)

        print(lidar.info)

        for scan in lidar.iter_scans():
            post(client, scan)
    except RPLidarException:
        lidar.stop()
        lidar.disconnect()
        collect_data(client)
    except KeyboardInterrupt:
        print('Stopping.')
        lidar.stop()
        lidar.disconnect()


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")

        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client


def post(client, data):
    try:
        v = json.dumps(data)

        result = client.publish(topic, v)
        # result: [0, 1]
        status = result[0]
        if status == 1:
            print(f"Failed to send message to topic {topic}")
    except (ConnectionResetError, ConnectionRefusedError) as err:
        print(err)


def run():
    print("hello mqtt lidar!")
    client = connect_mqtt()
    client.loop_start()



    try:
        collect_data(client)
    except RPLidarException as err:
        print(err)
        led.value = False


if __name__ == '__main__':
    run()
