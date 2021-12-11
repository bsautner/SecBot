package com.secbot.core

/**
 * Serial1 << "LDR," << distance << "," << angle  << "," << startBit << ","  << quality << "\n" ;
 */

enum class Source {
    PING,
    PONG,
    LDR,
    MAG_SERIAL,
    MAG_PI,
    CMP,
    ACC_PI,
    ACC_SERIAL,
    MQTT;

}