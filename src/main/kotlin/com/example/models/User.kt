package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(val id: Int, val username: String, val password: String)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 16)
    val password = varchar("password", 64)
}

fun ResultRow.toUser() = User(
    id = this[Users.id],
    username = this[Users.username],
    password = this[Users.password],
)