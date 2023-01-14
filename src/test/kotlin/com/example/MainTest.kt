package com.example

import com.example.models.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

fun main() {

    val module = SerializersModule {
        polymorphic(DataModel::class) {
            subclass(News::class)
            subclass(User::class)
        }
    }

    val format = Json { serializersModule = module }

    val news = News(id = 0, title = "title", body = "body", categoryId = 2)

    println(format.encodeToString(news))

    val response = ResponseModel(
        state = State.Success,
        result = news
    )

    println(format.encodeToString(response))


    println(mapOf("response" to response))
}