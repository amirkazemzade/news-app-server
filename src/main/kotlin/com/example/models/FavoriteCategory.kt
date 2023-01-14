package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class FavoriteCategory(
    val id: Int = 0,
    val userId: Int,
    val category: Category
) : DataModel() {
    fun copyWith(
        id: Int? = null,
        userId: Int? = null,
        category: Category? = null,
    ) = FavoriteCategory(
        id ?: this.id,
        userId ?: this.userId,
        category ?: this.category,
    )
}

object FavoriteCategories : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val category = enumeration<Category>("category")
}

fun ResultRow.toFavoriteCategory() = FavoriteCategory(
    id = this[FavoriteCategories.id],
    userId = this[FavoriteCategories.userId],
    category = this[FavoriteCategories.category],
)