package com.secbot.api.servlets

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RootServlet {

    @GetMapping(value = ["/"], produces = ["text/plain"])
    fun index(): String {
         return "Hello there"
    }
}