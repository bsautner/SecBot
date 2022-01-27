import RPi.GPIO as GPIO
import time

LED_PIN = 36


# GPIO.setmode(GPIO.BOARD)
# GPIO.setup(LED_PIN, GPIO.OUT)
# GPIO.output(LED_PIN, GPIO.HIGH)
# time.sleep(5)
# GPIO.output(LED_PIN, GPIO.LOW)
# GPIO.cleanup()
# print("LED off")

def on():
    # GPIO.setmode(GPIO.BOARD)
    GPIO.setup(LED_PIN, GPIO.OUT)
    GPIO.output(LED_PIN, GPIO.HIGH)
    print("LED ON")
