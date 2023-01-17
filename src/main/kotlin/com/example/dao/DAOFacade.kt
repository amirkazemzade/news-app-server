package com.example.dao

import com.example.models.FavoriteCategory
import com.example.models.News
import com.example.models.User


interface DAOFacade {

    /* User */
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun user(user: User): User?
    suspend fun user(username: String): User?
    suspend fun createUser(user: User): User?
    suspend fun editUser(user: User): Boolean
    suspend fun deleteUser(id: Int): Boolean

    /* News */
    suspend fun editNews(news: News): Boolean
    suspend fun allNews(): List<News>
    suspend fun news(id: Int): News?
    suspend fun createNews(news: News): News?
    suspend fun deleteNews(id: Int): Boolean

    /* User News */
    suspend fun userAllNews(userId: Int): List<News>

    /* Favorite Category */
    suspend fun allFavoriteCategories(): List<FavoriteCategory>
    suspend fun favoriteCategory(id: Int): FavoriteCategory?
    suspend fun favoriteCategory(userId: Int, categoryId: Int): FavoriteCategory?
    suspend fun createFavoriteCategory(favoriteCategory: FavoriteCategory): FavoriteCategory?
    suspend fun editFavoriteCategory(favoriteCategory: FavoriteCategory): Boolean
    suspend fun deleteFavoriteCategory(id: Int): Boolean
    suspend fun fetchUserCategories(userId: Int): List<FavoriteCategory>
}