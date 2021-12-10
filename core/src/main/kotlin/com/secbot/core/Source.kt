package com.secbot.core

/**
 * Serial1 << "LDR," << distance << "," << angle  << "," << startBit << ","  << quality << "\n" ;
 */

enum class Source {
    LDR,
    MAG,
    CMP,
    ACC,
    MQTT;

    fun matches(txt: String) : Boolean {
        return (this.name == txt)
    }
}