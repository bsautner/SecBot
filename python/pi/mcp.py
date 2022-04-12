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



# from adafruit_mcp230xx.mcp23017 import MCP23017


# Initialize the I2C bus:
i2c = busio.I2C(board.SCL, board.SDA)

# Create an instance of either the MCP23008 or MCP23017 class depending on
# which chip you're using:
mcp = MCP23008(i2c)  # MCP23008
# mcp = MCP23017(i2c)  # MCP23017

# Optionally change the address of the device if you set any of the A0, A1, A2
# pins.  Specify the new address with a keyword parameter:
# mcp = MCP23017(i2c, address=0x21)  # MCP23017 w/ A0 set

# Now call the get_pin function to get an instance of a pin on the chip.
# This instance will act just like a digitalio.DigitalInOut class instance
# and has all the same properties and methods (except you can't set pull-down
# resistors, only pull-up!).  For the MCP23008 you specify a pin number from 0
# to 7 for the GP0...GP7 pins.  For the MCP23017 you specify a pin number from
# 0 to 15 for the GPIOA0...GPIOA7, GPIOB0...GPIOB7 pins (i.e. pin 12 is GPIOB4).
pin0 = mcp.get_pin(0)
pin1 = mcp.get_pin(1)
pin2 = mcp.get_pin(2)
pin3 = mcp.get_pin(3)
pin4 = mcp.get_pin(4)
pin5 = mcp.get_pin(5)
pin6 = mcp.get_pin(6)
pin7 = mcp.get_pin(7)


# Setup pin0 as an output that's at a high logic level.
pin0.switch_to_output(value=True)
pin1.switch_to_output(value=True)
pin2.switch_to_output(value=True)
pin3.switch_to_output(value=True)
pin4.switch_to_output(value=True)
pin5.switch_to_output(value=True)
pin6.switch_to_output(value=True)
pin7.switch_to_output(value=True)
# pin3.switch_to_input()
# Setup pin1 as an input with a pull-up resistor enabled.  Notice you can also
# use properties to change this state.
# pin1.direction = digitalio.Direction.INPUT
# pin1.pull = digitalio.Pull.UP

# Now loop blinking the pin 0 output and reading the state of pin 1 input.
while True:
    # Blink pin 0 on and then off.
    pin0.value = True
    pin1.value = True
    pin2.value = True
    pin3.value = True
    pin4.value = True
    pin5.value = True
    pin6.value = True
    pin7.value = True
    print("Pin 0 is at a high level: {0}".format(pin0.value))
    print("Pin 1 is at a high level: {0}".format(pin1.value))
    time.sleep(3)
    pin0.value = False
    pin1.value = False
    pin2.value = False
    pin3.value = False
    pin4.value = False
    pin5.value = False
    pin6.value = False
    pin7.value = False
    time.sleep(3)
    print("Pin 0 is at a high level: {0}".format(pin0.value))
    # Read pin 1 and print its state.
    print("Pin 1 is at a high level: {0}".format(pin1.value))
