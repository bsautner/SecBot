import time
import board
import pwmio
from adafruit_motor import servo

# create a PWMOut object on Pin D5.
pwm = pwmio.PWMOut(board.D5, frequency=50)

# Create a servo object.
servo = servo.Servo(pwm)

while True:
    servo.angle = 170
    print("right")
    time.sleep(4)
    servo.angle = 90
    print("forward")
    time.sleep(4)
    servo.angle = 20
    print("left")
    time.sleep(4)

    # for angle in range(10, 45, 5):  # 0 - 180 degrees, 5 degrees at a time.
    #     servo.angle = angle
    #     time.sleep(0.05)
    # for angle in range(-45, 10, -5): # 180 - 0 degrees, 5 degrees at a time.
    #     servo.angle = angle
    #     time.sleep(0.05)
