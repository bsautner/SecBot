import RPi.GPIO as GPIO
import time

LED_PIN = 18
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)


# GPIO.setmode(GPIO.BOARD)
# GPIO.setup(LED_PIN, GPIO.OUT)
# GPIO.output(LED_PIN, GPIO.HIGH)
# time.sleep(5)
# GPIO.output(LED_PIN, GPIO.LOW)
# GPIO.cleanup()
# print("LED off")

def on():
    GPIO.output(LED_PIN, GPIO.HIGH)
    print("LED ON")


def off():
    GPIO.output(LED_PIN, GPIO.LOW)
    GPIO.cleanup()
    print("LED OFF")


def blink(interval):
    on()
    time.sleep(interval)
    off()
    time.sleep(interval)
