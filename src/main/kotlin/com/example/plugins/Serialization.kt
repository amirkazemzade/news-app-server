package com.example.plugins

import com.example.models.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

fun Application.configureSerialization() {
    val module = SerializersModule {
        polymorphic(DataModel::class) {
            subclass(News::class)
            subclass(User::class)
            subclass(FavoriteCategory::class)
            subclass(TokenModel::class)
            subclass(CategoriesModel::class)
        }
    }
    install(ContentNegotiation) {
        json(
            json = Json {
                serializersModule = module
            }
        )
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
