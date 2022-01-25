import RPi.GPIO as GPIO
from math import cos, sin, pi, floor
from adafruit_rplidar import RPLidar
from adafruit_rplidar import RPLidarException
import socket
from time import sleep
import threading
import time
import board
import digitalio
import json
import random
import paho.mqtt.client as mqtt_client
import network.mqtt as mqtt
import sensors.sonar as sonar

running = False

broker = "localhost"
port = 1883
topic = "RAW_LIDAR"
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'

PORT_NAME = '/dev/ttyUSB0'
scan_data = [0] * 360
lidar_motor = digitalio.DigitalInOut(board.D4)
lidar_motor.direction = digitalio.Direction.OUTPUT


def init():
    print("Shutting Down!")
    lidar_motor.value = False


    print("Starting up!")
    running = True
    m = threading.Thread(target=mqtt.run())
    m.setDaemon(True)
    m.start()


    sonarProcess = threading.Thread(target=sonar.run())
    sonarProcess.setDaemon(True)
    sonarProcess.start()




    while running:
        sleep(0.1)


def run():
   init()


if __name__ == '__main__':
    run()
