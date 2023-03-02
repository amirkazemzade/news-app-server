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

    override suspend fun user(user: User): User? = dbQuery {
        Users.select { Users.username eq user.username }
            .andWhere { Users.password eq user.password }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun user(username: String): User? = dbQuery {
        Users.select { Users.username eq username }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun createUser(user: User): User? = dbQuery {
        val insertStatement = Users.insert { table ->
            user.id?.let {
                table[id] = it
            }
            table[username] = user.username
            table[password] = user.password
            table[isAdmin] = user.isAdmin ?: false
        }
        insertStatement.resultedValues?.singleOrNull()?.toUser()
    }

    override suspend fun editUser(user: User): Boolean = dbQuery {
        Users.update({ Users.id eq user.id!! }) {
            it[username] = user.username
            it[password] = user.password
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

    override suspend fun createNews(news: News): News? =
        dbQuery {
            val insertStatement = NewsTable.insert { table ->
                news.id?.let {
                    table[id] = it
                }
                table[title] = news.title
                table[body] = news.body
                table[categoryId] = news.categoryId
                table[viewCount] = news.viewCount
            }
            insertStatement.resultedValues?.singleOrNull()?.toNews()
        }

    override suspend fun editNews(news: News): Boolean =
        dbQuery {
            NewsTable.update({ NewsTable.id eq news.id!! }) {
                it[title] = news.title
                it[body] = news.body
                it[categoryId] = news.categoryId
                it[viewCount] = news.viewCount
            } > 0
        }

    override suspend fun deleteNews(id: Int): Boolean = dbQuery {
        NewsTable.deleteWhere { NewsTable.id eq id } > 0
    }

    override suspend fun userAllNews(userId: Int): List<News> = dbQuery {
        val favoriteCategories = FavoriteCategories
            .slice(FavoriteCategories.categoryId)
            .select { FavoriteCategories.userId eq userId }
        println(favoriteCategories)
        NewsTable
            .select { NewsTable.categoryId inSubQuery favoriteCategories }
            .orderBy(NewsTable.categoryId)
            .orderBy(NewsTable.viewCount, SortOrder.DESC)
            .map { it.toNews() }
    }

    override suspend fun allFavoriteCategories(): List<FavoriteCategory> = dbQuery {
        FavoriteCategories.selectAll().map { it.toFavoriteCategory() }
    }

    override suspend fun favoriteCategory(id: Int): FavoriteCategory? = dbQuery {
        FavoriteCategories.select { FavoriteCategories.id eq id }.map { it.toFavoriteCategory() }.singleOrNull()
    }

    override suspend fun favoriteCategory(userId: Int, categoryId: Int): FavoriteCategory? = dbQuery {
        FavoriteCategories.select { FavoriteCategories.userId eq userId }
            .andWhere { FavoriteCategories.categoryId eq categoryId }
            .map { it.toFavoriteCategory() }
            .singleOrNull()
    }

    override suspend fun createFavoriteCategory(favoriteCategory: FavoriteCategory): FavoriteCategory? =
        dbQuery {
            val insertStatement = FavoriteCategories.insert {
                it[userId] = favoriteCategory.userId
                it[categoryId] = favoriteCategory.categoryId
            }
            insertStatement.resultedValues?.singleOrNull()?.toFavoriteCategory()
        }


    override suspend fun editFavoriteCategory(favoriteCategory: FavoriteCategory): Boolean =
        dbQuery {
            FavoriteCategories.update({ FavoriteCategories.id eq favoriteCategory.id!! }) {
                it[userId] = favoriteCategory.userId
                it[categoryId] = favoriteCategory.categoryId
            } > 0
        }

    override suspend fun deleteFavoriteCategory(id: Int): Boolean = dbQuery {
        FavoriteCategories.deleteWhere { FavoriteCategories.id eq id } > 0
    }

    override suspend fun fetchUserCategories(userId: Int): List<FavoriteCategory> = dbQuery {
        FavoriteCategories.select { FavoriteCategories.userId eq userId }.map { it.toFavoriteCategory() }
    }
}

val dao: DAOFacade = DAOFacadeImpl()
