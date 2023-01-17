package com.example.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
@SerialName("User")
data class User @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: Int? = null,
    val username: String,
    val password: String,
    @EncodeDefault(mode = EncodeDefault.Mode.NEVER)
    val isAdmin: Boolean = false,
) : DataModel {

    fun copyWith(
        id: Int? = null,
        username: String? = null,
        password: String? = null,
        isAdmin: Boolean? = null,
    ): User = User(
        id = id ?: this.id,
        username = username ?: this.username,
        password = password ?: this.password,
        isAdmin = isAdmin ?: this.isAdmin,
    )
}

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 16)
    val password = varchar("password", 64)
    val isAdmin = bool("is_admin")
}

fun ResultRow.toUser() = User(
    id = this[Users.id],
    username = this[Users.username],
    password = this[Users.password],
    isAdmin = this[Users.isAdmin],
)