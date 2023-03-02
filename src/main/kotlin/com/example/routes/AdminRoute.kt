package com.example.routes

import com.example.dao.dao
import com.example.exceptions.NotFoundException
import com.example.exceptions.UnauthorizedException
import com.example.findUser
import com.example.models.DataListModel
import com.example.models.News
import com.example.models.ResponseModel
import com.example.models.State
import com.example.responseWithBoolean
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*

fun Routing.adminRoute() {

    authenticate {
        route("admin/news") {
            get {
                checkAdmin()
                call.respond(
                    ResponseModel(
                        state = State.Success,
                        result = DataListModel(
                            items = dao.allNews().map { it.withoutBody() }
                        )
                    ),
                )
            }

            get("{id}") {
                checkAdmin()
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
                checkAdmin()
                val news = call.receive<News>()
                val createdNews = dao.createNews(news)
                call.respondRedirect("/admin/news/${createdNews?.id}")
            }

            patch("{id}") {
                checkAdmin()
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
                responseWithBoolean(updated)
            }

            delete("{id}") {
                checkAdmin()
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val deleted = dao.deleteNews(id)
                responseWithBoolean(deleted)
            }
        }
    }
}


private suspend fun PipelineContext<Unit, ApplicationCall>.checkAdmin() {
    val principal = call.principal<JWTPrincipal>()
    val user = findUser(principal)
    if (user.isAdmin != true) throw UnauthorizedException
}
