package com.example

import com.example.dao.DatabaseFactory
import com.example.dao.dao
import kotlinx.coroutines.runBlocking

suspend fun main() {

//    val module = SerializersModule {
//        polymorphic(DataModel::class) {
//            subclass(News::class)
//            subclass(User::class)
//        }
//    }
//
//    val format = Json { serializersModule = module }
//
//    val news = News(id = 0, title = "title", body = "body", categoryId = 2)
//
//    println(format.encodeToString(news))
//
//    val response = ResponseModel(
//        state = State.Success,
//        result = news
//    )
    getUserNews()

//    println(format.encodeToString(response))


//    println(mapOf("response" to response))
}

fun getUserNews() = runBlocking {
    DatabaseFactory.init()
    val userNews = dao.userAllNews(2)
    println(userNews)
}