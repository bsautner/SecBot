import time

import adafruit_hcsr04
import board
import py.pi.device.led as led

left_front_sonar = adafruit_hcsr04.HCSR04(trigger_pin=board.D22, echo_pin=board.D11)
right_front_sonar = adafruit_hcsr04.HCSR04(trigger_pin=board.D22, echo_pin=board.D13)
front_sonar = adafruit_hcsr04.HCSR04(trigger_pin=board.D22, echo_pin=board.D15)
down_front_sonar = adafruit_hcsr04.HCSR04(trigger_pin=board.D22, echo_pin=board.D5)
sonars = [left_front_sonar, right_front_sonar, front_sonar, down_front_sonar]
running = False
topic = "sonar"


def run(mqtt):
    print("Started Sonar Thread")
    while True:
        try:
            lf = left_front_sonar.distance
            mqtt.publish(topic, f'left_front_sonar,{lf}')
            time.sleep(.5)
            rf = right_front_sonar.distance
            mqtt.publish(topic, f'right_front_sonar,{rf}')
            time.sleep(.5)
            fs = front_sonar.distance
            mqtt.publish(topic, f'front_sonar,{fs}')
            time.sleep(.5)
            d = down_front_sonar.distance
            mqtt.publish(topic, f'down_front_sonar,{d}')
            time.sleep(.5)

            if lf < 10 or rf < 10 or fs < 10 or d < 10:
                led.blink(led.BLUE, 1, 0.1)
            else:
                led.blink(led.GREEN, 1, 0.1)
        except RuntimeError:
            print("Retrying Sonar...")

