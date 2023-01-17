package com.example.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
@SerialName("News")
data class News @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: Int? = null,
    val title: String,
    val body: String,
    @SerialName("category_id") val categoryId: Int,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("view_count") val viewCount: Int = 0,
) : DataModel {
    fun copyWith(
        id: Int? = null,
        title: String? = null,
        body: String? = null,
        categoryId: Int? = null,
        viewCount: Int? = null,
    ): News = News(
        id = id ?: this.id,
        title = title ?: this.title,
        body = body ?: this.body,
        categoryId = categoryId ?: this.categoryId,
        viewCount = viewCount ?: this.viewCount,
    )

    fun withoutBody() = NewsWithoutBody(
        id = id,
        title = title,
        categoryId = categoryId,
        viewCount = viewCount,
    )
}

object NewsTable : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val body = varchar("body", 4096)
    val categoryId = integer("category_id")
    val viewCount = integer("view_count")
}

fun ResultRow.toNews() = News(
    id = this[NewsTable.id],
    title = this[NewsTable.title],
    body = this[NewsTable.body],
    categoryId = this[NewsTable.categoryId],
    viewCount = this[NewsTable.viewCount]
)