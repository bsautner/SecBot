from math import cos, sin, pi, floor
from adafruit_rplidar import RPLidar
import socket
from time import sleep
import time
import board
import digitalio

PORT_NAME = '/dev/ttyUSB0'
lidar = RPLidar(None, PORT_NAME)

# used to scale data to fit on the screen
max_distance = 0
scan_data = [0]*360


led = digitalio.DigitalInOut(board.D7)
led.direction = digitalio.Direction.OUTPUT

def collect_data():
    try:
        print(lidar.info)
        for scan in lidar.iter_scans():
            for (_, angle, distance) in scan:
                scan_data[min([359, floor(angle)])] = distance
            process_data(scan_data)

    except KeyboardInterrupt:
        print('Stoping.')
    lidar.stop()
    lidar.disconnect()


def process_data(data):
    global max_distance

    for angle in range(360):
        distance = data[angle]
        if distance > 0:                  # ignore initially ungathered data points
            max_distance = max([min([5000, distance]), max_distance])
            radians = angle * pi / 180.0
            x = distance * cos(radians)
            y = distance * sin(radians)
            point = (160 + int(x / max_distance * 119), 120 + int(y / max_distance * 119))
            print(point[0])



def run():
    print("hello lidar")
    led.value = True
    collect_data()


if __name__ == '__main__':
    run()