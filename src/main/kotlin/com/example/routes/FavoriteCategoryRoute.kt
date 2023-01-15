package com.example.routes

import com.example.dao.dao
import com.example.exceptions.NotFoundException
import com.example.models.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.favoriteCategoryRoute() {
    authenticate {
        route("fav-cat") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val user = findUser(principal)
                val categories = fetchFavoriteCategories(user.id!!)
                call.respond(ResponseModel(state = State.Success, result = categories))
            }

            post {
                val addingCategories = call.receive<CategoriesModel>()
                val principal = call.principal<JWTPrincipal>()
                val user = findUser(principal)

                validateCategories(addingCategories)

                val currentCategories = fetchFavoriteCategories(user.id!!)

                val (newCategories, removingCategories) = uncommonCategories(
                    addingCategories.categories.toSet(),
                    currentCategories.categories.toSet()
                )

                newCategories.forEach { categoryId ->
                    dao.createFavoriteCategory(FavoriteCategory(userId = user.id, categoryId = categoryId))
                }

                removingCategories.forEach { categoryId ->
                    val favoriteCategory = dao.favoriteCategory(user.id, categoryId)
                    dao.deleteFavoriteCategory(favoriteCategory?.id ?: throw NotFoundException)
                }

                call.respond(
                    ResponseModel(
                        state = State.Success,
                        message = "Created Successfully"
                    )
                )

            }
        }
    }
}

private fun validateCategories(categories: CategoriesModel) {
    categories.categories.forEach { categoryId ->
        Category.fromId(categoryId) ?: throw NotFoundException
    }
}

private fun uncommonCategories(
    addingCategories: Set<Int>,
    currentCategories: Set<Int>
): Pair<Set<Int>, Set<Int>> {
    val commonCategories = addingCategories.intersect(currentCategories)
    val newCategories = addingCategories.minus(commonCategories)
    val removingCategories = currentCategories.minus(commonCategories)
    return Pair(newCategories, removingCategories)
}

private suspend fun fetchFavoriteCategories(userId: Int): CategoriesModel {
    val favoriteCategories = dao.fetchUserCategories(userId)
    val categories = CategoriesModel(
        categories = favoriteCategories.map {
            val category = Category.fromId(it.categoryId) ?: throw NotFoundException
            category.id
        }.toList()
    )
    return categories
}

private suspend fun findUser(principal: JWTPrincipal?): User {
    val username = principal!!.payload.getClaim("username").asString()
    return dao.user(username) ?: throw NotFoundException
}