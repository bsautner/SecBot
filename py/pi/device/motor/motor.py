import time
import RPi.GPIO as GPIO

power = 0


class Motor:

    def __init__(self, p1, p2):
        self.p1 = p1
        self.p2 = p2

    def stop(self):
        print("i'm stopping")
        self.p1.ChangeDutyCycle(0)
        self.p2.ChangeDutyCycle(0)

    def f(self):
        print("going forward")
        self.p1.ChangeDutyCycle(power)
        self.p2.ChangeDutyCycle(0)

    def r(self):
        print("going reverse")
        self.p1.ChangeDutyCycle(0)
        self.p2.ChangeDutyCycle(power)

    def move(self, command):
        print("Moving")
        print(command['motion'])
        if command['motion'] == "FORWARD":
            self.f()
        elif command['motion'] == "REVERSE":
            self.r()
        elif command['motion'] == "STOP":
            self.stop()


def test():
    print("Testing Motor")

    print("Done Testing Motor")


if __name__ == '__main__':
    test()
