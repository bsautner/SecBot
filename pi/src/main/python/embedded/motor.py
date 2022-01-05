import time
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BOARD)
GPIO.setup(32, GPIO.OUT)
GPIO.setup(33, GPIO.OUT)

p = GPIO.PWM(32, 50)  # channel=12 frequency=50Hz
p.start(0)

p2 = GPIO.PWM(33, 50)  # channel=12 frequency=50Hz
p2.start(0)

p.ChangeDutyCycle(50)
print(100)
time.sleep(10)
print(0)
p.ChangeDutyCycle(0)
time.sleep(10)

p.stop()

p2.ChangeDutyCycle(50)
print(100)
time.sleep(10)
print(0)
p2.ChangeDutyCycle(0)
time.sleep(10)

p2.stop()
GPIO.cleanup()
