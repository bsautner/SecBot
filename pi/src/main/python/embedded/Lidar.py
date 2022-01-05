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


HOST = '127.0.0.1'  # The server's hostname or IP address
PORT = 2323        # The port used by the server


PORT_NAME = '/dev/ttyUSB0'
lidar = RPLidar(None, PORT_NAME)

# used to scale data to fit on the screen
max_distance = 0
scan_data = [0]*360
led = digitalio.DigitalInOut(board.D4)
led.direction = digitalio.Direction.OUTPUT
# quality, angle, distance https://github.com/adafruit/Adafruit_CircuitPython_RPLIDAR/blob/208b16f92b6491d3d950d587bc9eb60ea2d51251/adafruit_rplidar.py#L508
def collect_data():
    try:
        print(lidar.info)

        for scan in lidar.iter_scans():
            print(scan)
            post(scan)
            # for (_, angle, distance) in scan:
            #     scan_data[min([359, floor(angle)])] = distance
                # try:
                #     # print("processing")
                #     # process_data(scan_data)
                #     post(scan_data)
                # except RPLidarException as err:
                #     print(err)


    except KeyboardInterrupt:
        print('Stoping.')
    lidar.stop()
    lidar.disconnect()

def post(data):
    try:
        v = json.dumps(data)
        byte_message = bytes(v, 'utf-8')
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((HOST, PORT))
            s.sendall(byte_message)
    except (ConnectionResetError, ConnectionRefusedError) as err:
        print(err)
#
# def process_data(data):
#     global max_distance
#
#     for angle in range(360):
#         distance = data[angle]
#
#         # if distance > 0:                  # ignore initially ungathered data points
#             # post(angle, distance)
#             # max_distance = max([min([5000, distance]), max_distance])
#             # radians = angle * pi / 180.0
#             # x = distance * cos(radians)
#             # y = distance * sin(radians)
#             # point = (160 + int(x / max_distance * 119), 120 + int(y / max_distance * 119))
#



def run():
    print("hello lidar")
    led.value = True
    sleep(2)
    try:
        collect_data()
    except RPLidarException as err:
        print(err)
        led.value = False




if __name__ == '__main__':
    run()