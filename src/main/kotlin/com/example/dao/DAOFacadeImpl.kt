package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class DAOFacadeImpl : DAOFacade {
    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map { it.toUser() }
    }

    override suspend fun user(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }.map { it.toUser() }.singleOrNull()
    }

    override suspend fun createUser(username: String, password: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.username] = username
            it[Users.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.toUser()
    }

    override suspend fun editUser(id: Int, username: String, password: String): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[Users.id] = id
            it[Users.username] = username
            it[Users.password] = password
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    override suspend fun allNews(): List<News> = dbQuery {
        NewsTable.selectAll().map { it.toNews() }
    }

    override suspend fun news(id: Int): News? = dbQuery {
        NewsTable.select { NewsTable.id eq id }.map { it.toNews() }.singleOrNull()
    }

    override suspend fun createNews(title: String, body: String, category: Category, viewCount: Int): News? =
        dbQuery {
            val insertStatement = NewsTable.insert {
                it[NewsTable.title] = title
                it[NewsTable.body] = body
                it[NewsTable.category] = category
                it[NewsTable.viewCount] = viewCount
            }
            insertStatement.resultedValues?.singleOrNull()?.toNews()
        }

    override suspend fun editNews(id: Int, title: String, body: String, category: Category, viewCount: Int): Boolean =
        dbQuery {
            NewsTable.update({ NewsTable.id eq id }) {
                it[NewsTable.id] = id
                it[NewsTable.title] = title
                it[NewsTable.body] = body
                it[NewsTable.category] = category
                it[NewsTable.viewCount] = viewCount
            } > 0
        }

    override suspend fun deleteNews(id: Int): Boolean = dbQuery {
        NewsTable.deleteWhere { NewsTable.id eq id } > 0
    }

    override suspend fun allFavoriteCategories(): List<FavoriteCategory> = dbQuery {
        FavoriteCategories.selectAll().map { it.toFavoriteCategory() }
    }

    override suspend fun favoriteCategory(id: Int): FavoriteCategory? = dbQuery {
        FavoriteCategories.select { FavoriteCategories.id eq id }.map { it.toFavoriteCategory() }.singleOrNull()
    }

    override suspend fun createFavoriteCategory(userId: Int, category: Category): FavoriteCategory? =
        dbQuery {
            val insertStatement = FavoriteCategories.insert {
                it[FavoriteCategories.userId] = userId
                it[FavoriteCategories.category] = category
            }
            insertStatement.resultedValues?.singleOrNull()?.toFavoriteCategory()
        }


    override suspend fun editFavoriteCategory(id: Int, userId: Int, category: Category): Boolean =
        dbQuery {
            FavoriteCategories.update({ FavoriteCategories.id eq id }) {
                it[FavoriteCategories.id] = id
                it[FavoriteCategories.userId] = userId
                it[FavoriteCategories.category] = category
            } > 0
        }

    override suspend fun deleteFavoriteCategory(id: Int): Boolean = dbQuery {
        FavoriteCategories.deleteWhere { FavoriteCategories.id eq id } > 0
    }

}

val dao: DAOFacade = DAOFacadeImpl()
