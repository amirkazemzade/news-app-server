package com.example.routes

import com.example.dao.dao
import com.example.exceptions.DefaultException
import com.example.exceptions.NotFoundException
import com.example.findUser
import com.example.models.DataListModel
import com.example.models.ResponseModel
import com.example.models.State
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.newsRoute() {
    authenticate {
        route("news") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val user = findUser(principal)
                call.respond(
                    ResponseModel(
                        state = State.Success,
                        result = DataListModel(
                            items = dao.userAllNews(user.id!!).map { it.withoutBody() }
                        )
                    )
                )
            }

            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val news = dao.news(id) ?: throw NotFoundException
                val viewedNews = news.copyWith(viewCount = news.viewCount + 1)
                val isUpdated = dao.editNews(viewedNews)
                if (!isUpdated) throw DefaultException
                call.respond(
                    ResponseModel(
                        state = State.Success,
                        result = viewedNews
                    ),
                )
            }
        }
    }
}
