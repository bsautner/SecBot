# SPDX-FileCopyrightText: 2017 Tony DiCola for Adafruit Industries
#
# SPDX-License-Identifier: MIT

# Simple demo of reading and writing the digital I/O of the MCP2300xx as if
# they were native CircuitPython digital inputs/outputs.
# Author: Tony DiCola
import time

import board
import busio
import digitalio
import adafruit_hcsr04
from adafruit_mcp230xx.mcp23008 import MCP23008


delay = 0.5
# from adafruit_mcp230xx.mcp23017 import MCP23017


# Initialize the I2C bus:
i2c = busio.I2C(board.SCL, board.SDA)

# Create an instance of either the MCP23008 or MCP23017 class depending on
# which chip you're using:
mcp = MCP23008(i2c)  # MCP23008
# mcp = MCP23017(i2c)  # MCP23017
time.sleep(1)
# Optionally change the address of the device if you set any of the A0, A1, A2
# pins.  Specify the new address with a keyword parameter:
# mcp = MCP23017(i2c, address=0x21)  # MCP23017 w/ A0 set

# Now call the get_pin function to get an instance of a pin on the chip.
# This instance will act just like a digitalio.DigitalInOut class instance
# and has all the same properties and methods (except you can't set pull-down
# resistors, only pull-up!).  For the MCP23008 you specify a pin number from 0
# to 7 for the GP0...GP7 pins.  For the MCP23017 you specify a pin number from
# 0 to 15 for the GPIOA0...GPIOA7, GPIOB0...GPIOB7 pins (i.e. pin 12 is GPIOB4).
fan_pin = mcp.get_pin(0)
pin1 = mcp.get_pin(1)
pin2 = mcp.get_pin(2)
pin3 = mcp.get_pin(3)
pin4 = mcp.get_pin(4)
pin5 = mcp.get_pin(5)
pin6 = mcp.get_pin(6)
pin7 = mcp.get_pin(7)


# Setup pin0 as an output that's at a high logic level.
fan_pin.switch_to_output(value=False)
pin1.switch_to_output(value=False)
pin2.switch_to_output(value=False)
pin3.switch_to_output(value=False)
pin4.switch_to_output(value=False)
pin5.switch_to_output(value=False)
pin6.switch_to_output(value=False)
pin7.switch_to_output(value=False)
# pin3.switch_to_input()
# Setup pin1 as an input with a pull-up resistor enabled.  Notice you can also
# use properties to change this state.
# pin1.direction = digitalio.Direction.INPUT
# pin1.pull = digitalio.Pull.UP

# Now loop blinking the pin 0 output and reading the state of pin 1 input.
time.sleep(1)
while True:
    # Blink pin 0 on and then off.
    fan_pin.value = True
    time.sleep(delay)
    print("Pin 0 is: {}".format(fan_pin.value))
    pin1.value = True
    print("Pin 1 is: {}".format(pin1.value))
    time.sleep(delay)
    pin2.value = True
    print("Pin 2 is: {}".format(pin2.value))
    time.sleep(delay)
    pin3.value = True
    print("Pin 3 is: {}".format(pin3.value))
    time.sleep(delay)
    pin4.value = True
    print("Pin 4 is: {}".format(pin4.value))
    time.sleep(delay)
    pin5.value = True
    print("Pin 5 is: {}".format(pin5.value))
    time.sleep(delay)
    pin6.value = True
    print("Pin 6 is: {}".format(pin6.value))
    time.sleep(delay)
    pin7.value = True
    print("Pin 7 is: {}".format(pin7.value))

    time.sleep(3)
    fan_pin.value = False
    time.sleep(delay)
    pin1.value = False
    time.sleep(delay)
    pin2.value = False
    time.sleep(delay)
    pin3.value = False
    time.sleep(delay)
    pin4.value = False
    time.sleep(delay)
    pin5.value = False
    time.sleep(delay)
    pin6.value = False
    time.sleep(delay)
    pin7.value = False
    time.sleep(delay)
    time.sleep(3)
    print("Pin 0 is at a high level: {0}".format(fan_pin.value))
    # Read pin 1 and print its state.
    print("Pin 1 is at a high level: {0}".format(pin1.value))
