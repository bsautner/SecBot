import time
import board
import adafruit_hcsr04
import RPi.GPIO as GPIO

# Use BCM GPIO references
# instead of physical pin numbers
# GPIO.setmode(GPIO.BCM)
# GPIO.setwarnings(False)
# GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
# GPIO.setup(GPIO_ECHO, GPIO.IN)

sonar_1 = adafruit_hcsr04.HCSR04(trigger_pin=board.D14, echo_pin=board.D15)
sonar_2 = adafruit_hcsr04.HCSR04(trigger_pin=board.D14, echo_pin=board.D0)
sonar_3 = adafruit_hcsr04.HCSR04(trigger_pin=board.D14, echo_pin=board.D13)
sonar_4 = adafruit_hcsr04.HCSR04(trigger_pin=board.D14, echo_pin=board.D11)
time.sleep(1)

while True:
    try:

        print("Distance 1: {0}".format(sonar_1.distance))

    except RuntimeError as e:
        time.sleep(0.01)
        print("RuntimeError 1: {0}".format(e))
    try:

        print("Distance 2: {0}".format(sonar_2.distance))

    except RuntimeError as e:
        time.sleep(0.01)
        print("RuntimeError 2 : {0}".format(e))
    try:

        print("Distance 3: {0}".format(sonar_3.distance))

    except RuntimeError as e:
        time.sleep(0.01)
        print("RuntimeError 3 : {0}".format(e))

    try:

        print("Distance 4: {0}".format(sonar_4.distance))

    except RuntimeError as e:
        time.sleep(0.01)
        print("RuntimeError 4 : {0}".format(e))



    time.sleep(1)

    # try:
    #
    #     print("Distance 7: {0}".format(sonar_7.distance))
    #
    # except RuntimeError as e:
    #     time.sleep(0.01)
