package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
@SerialName("Favorite Category")
data class FavoriteCategory(
    val id: Int? = null,
    val userId: Int,
    @SerialName("category_id") val categoryId: Int,
) : DataModel {
    fun copyWith(
        id: Int? = null,
        userId: Int? = null,
        categoryId: Int? = null,
    ) = FavoriteCategory(
        id ?: this.id,
        userId ?: this.userId,
        categoryId ?: this.categoryId,
    )
}

object FavoriteCategories : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val categoryId = integer("category_id")
}

fun ResultRow.toFavoriteCategory() = FavoriteCategory(
    id = this[FavoriteCategories.id],
    userId = this[FavoriteCategories.userId],
    categoryId = this[FavoriteCategories.categoryId],
)