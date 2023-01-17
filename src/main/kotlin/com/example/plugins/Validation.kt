package com.example.plugins

import com.example.exceptions.DefaultException
import com.example.exceptions.NotFoundException
import com.example.exceptions.UnauthorizedException
import com.example.models.ResponseModel
import com.example.models.State
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureValidation() {
    install(RequestValidation) {
        install(StatusPages) {
            exception<NotFoundException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.NotFound,
                    ResponseModel(state = State.Failure, message = cause.message)
                )
            }

            exception<NumberFormatException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    ResponseModel(state = State.Failure, message = cause.message)
                )
            }

            exception<UnauthorizedException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    ResponseModel(state = State.Failure, message = cause.message)
                )
            }

            exception<DefaultException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    ResponseModel(state = State.Failure, message = cause.message)
                )
            }
        }
    }
}

