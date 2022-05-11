import time

import board
import busio
import digitalio
from adafruit_mcp230xx.mcp23008 import MCP23008

# Create the I2C bus


time.sleep(1)

GREEN = 5
RED = 6
BLUE = 7
i2c = busio.I2C(board.SCL, board.SDA)
mcp = MCP23008(i2c)


def blink(pin, times, delay):
    led = mcp.get_pin(pin)
    led.switch_to_output(value=True)
    for i in range(times):
        led.value = not led.value
        time.sleep(delay)

    led.value = False


def run():
    delay = 0.5
    times = 10
    blink(GREEN, times, delay)
    blink(RED, times, delay)
    blink(BLUE, times, delay)
    while True:
        time.sleep(1)


if __name__ == '__main__':
    run()
