package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(val id: Int = 0, val username: String, val password: String) : DataModel() {

    fun copyWith(
        id: Int? = null, username: String? = null, password: String? = null,
    ): User = User(
        id ?: this.id,
        username ?: this.username,
        password ?: this.password,
    )
}

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