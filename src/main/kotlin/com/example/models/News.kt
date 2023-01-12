package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class News(
    val id: Int,
    val title: String,
    val body: String,
    val category: Category,
    val viewCount: Int = 0,
)

object NewsTable : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val body = varchar("body", 4096)
    val category = enumeration<Category>("category")
    val viewCount = integer("view_count")
}

fun ResultRow.toNews() = News(
    id = this[NewsTable.id],
    title = this[NewsTable.title],
    body = this[NewsTable.body],
    category = this[NewsTable.category],
    viewCount = this[NewsTable.viewCount]
)