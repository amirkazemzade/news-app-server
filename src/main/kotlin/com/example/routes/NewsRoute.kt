package com.example.routes

import com.example.dao.dao
import com.example.exceptions.NotFoundException
import com.example.models.News
import com.example.models.ResponseModel
import com.example.models.State
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.newsRoute() {
    route("news") {
        get {
            call.respond(mapOf("news_list" to dao.allNews()))
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val news = dao.news(id) ?: throw NotFoundException
            call.respond(
                ResponseModel(
                    state = State.Success,
                    result = news
                ),
            )
        }

        post {
            val news = call.receive<News>()
            val createdNews = dao.createNews(news)
            call.respondRedirect("/news/${createdNews?.id}")
        }

        patch("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val news = dao.news(id) ?: throw NotFoundException

            val formParameters = call.receiveParameters()
            val title = formParameters["title"]
            val body = formParameters["body"]
            val categoryId = formParameters["category"]?.toInt()
            val viewCount = formParameters["view_count"]?.toInt()
            val newsModel = news.copyWith(
                title = title,
                body = body,
                categoryId = categoryId,
                viewCount = viewCount
            )
            val updated = dao.editNews(newsModel)
            call.respond(updated)
        }

        delete("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(dao.deleteNews(id))
        }
    }
}
