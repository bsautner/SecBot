import sys
sys.path.append('../')

import adafruit_hcsr04
import board
import time
import network.mqtt as mqtt

sonar = adafruit_hcsr04.HCSR04(trigger_pin=board.D17, echo_pin=board.D27)
running = False
topic = "forward_sonar"

def run():
    running = True
    front_sonar_cm = -1.0
    while front_sonar_cm < 0.0:
        try:
            front_sonar_cm = sonar.distance
            print((f'Forward Sonar Distance: {front_sonar_cm}'))
            time.sleep(1)
        except RuntimeError:
            print("Retrying Sonar!")
    while (running):
        try:
            new_front_sonar_cm = sonar.distance
            if (new_front_sonar_cm < front_sonar_cm -1 or new_front_sonar_cm > front_sonar_cm + 1):
                front_sonar_cm = new_front_sonar_cm
                mqtt.publish(topic, front_sonar_cm)
            time.sleep(1)
        except RuntimeError:
            print(print("Retrying Sonar!"))

def stop():
    running = False