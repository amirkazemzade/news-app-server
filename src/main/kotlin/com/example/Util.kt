package com.example

import com.example.dao.dao
import com.example.exceptions.DefaultException
import com.example.exceptions.NotFoundException
import com.example.models.ResponseModel
import com.example.models.State
import com.example.models.User
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun findUser(principal: JWTPrincipal?): User {
    val username = principal!!.payload.getClaim("username").asString()
    return dao.user(username) ?: throw NotFoundException
}

suspend fun PipelineContext<Unit, ApplicationCall>.responseWithBoolean(
    updated: Boolean
) {
    if (!updated) throw DefaultException
    call.respond(
        ResponseModel(
            state = State.Success,
            message = "Updated"
        )
    )
}