package com.secbot.core.data

import com.secbot.core.hardware.Control

data class DeviceCommand(private val control: Control, private val value: Double)