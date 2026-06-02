package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*

fun main(args: Array<String>) {
    embeddedServer(
        factory = io.ktor.server.netty.Netty,
        port = 9292,
        host = "0.0.0.0",
        module = Application::rootModule
    ).start(wait = true)
}


fun Application.rootModule() {//Чтобы IDE успокоилось
    // Сюда вы будете добавлять routing { ... } и другие настройки
}
