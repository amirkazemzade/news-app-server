package com.example.plugins

import com.example.routes.favoriteCategoryRoute
import com.example.routes.newsRoute
import com.example.routes.userRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {

        get("/") {
            call.respondText("Hello World!!")
        }
        newsRoute()
        userRoute()
        favoriteCategoryRoute()
    }
}

