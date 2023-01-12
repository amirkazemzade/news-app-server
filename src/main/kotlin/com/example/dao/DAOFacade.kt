package com.example.dao

import com.example.models.Category
import com.example.models.FavoriteCategory
import com.example.models.News
import com.example.models.User


interface DAOFacade {

    /* User */
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun createUser(username: String, password: String): User?
    suspend fun editUser(id: Int, username: String, password: String): Boolean
    suspend fun deleteUser(id: Int): Boolean

    suspend fun allNews(): List<News>
    suspend fun news(id: Int): News?
    suspend fun createNews(
        title: String,
        body: String,
        category: Category,
        viewCount: Int = 0,
    ): News?

    /* News */
    suspend fun editNews(
        id: Int,
        title: String,
        body: String,
        category: Category,
        viewCount: Int,
    ): Boolean

    suspend fun deleteNews(id: Int): Boolean

    /* Favorite Category */
    suspend fun allFavoriteCategories(): List<FavoriteCategory>
    suspend fun favoriteCategory(id: Int): FavoriteCategory?
    suspend fun createFavoriteCategory(userId: Int, category: Category): FavoriteCategory?
    suspend fun editFavoriteCategory(id: Int, userId: Int, category: Category): Boolean
    suspend fun deleteFavoriteCategory(id: Int): Boolean

}