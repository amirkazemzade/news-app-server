package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.dao
import com.example.models.ResponseModel
import com.example.models.State
import com.example.models.TokenModel
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Routing.userRoute() {
    post("/login") {
        val loggingInUser = call.receive<User>()
        val user = dao.user(loggingInUser)
        if (user == null) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ResponseModel(
                    state = State.Failure,
                    message = "Username or Password is Invalid"
                ),
            )
            return@post
        }
        val token = createToken(user)
        call.respond(
            ResponseModel(
                state = State.Success,
                result = TokenModel(token!!)
            )
        )
    }

    post("sign-up") {
        val signingUser = call.receive<User>()
        val user = dao.user(signingUser)
        if (user != null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseModel(
                    state = State.Failure,
                    message = "A user with this username is already exits"
                ),
            )
            return@post
        }
        val createdUser = dao.createUser(signingUser)
        val token = createToken(createdUser!!)
        call.respond(
            ResponseModel(
                state = State.Success,
                result = TokenModel(token!!)
            )
        )
    }

    authenticate {
        get("/check") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }
}

private fun createToken(user: User): String? {
    return JWT.create()
        .withAudience("http://0.0.0.0:8080/hello")
        .withIssuer("http://0.0.0.0:8080/")
        .withClaim("username", user.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 3.6e+6.toInt()))
        .sign(Algorithm.HMAC256("secret"))
}
