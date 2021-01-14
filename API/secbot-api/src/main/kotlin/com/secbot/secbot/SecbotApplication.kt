package com.secbot.secbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class SecbotApplication

fun main(args: Array<String>) {
    runApplication<SecbotApplication>(*args)
}
