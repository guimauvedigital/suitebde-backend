package com.suitebde.database.users

import com.suitebde.models.application.SearchOptions
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IPaginationOptions
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*

class UsersDatabaseRepository(
    private val database: IDatabase,
) : IUsersRepository {

    init {
        database.transaction {
            SchemaUtils.create(Users)
        }
    }

    override suspend fun listLastLoggedBefore(date: Instant): List<User> =
        database.suspendedTransaction {
            Users
                .selectAll()
                .where { Users.lastLoginAt lessEq date }
                .map(Users::toUser)
        }

    override suspend fun create(payload: CreateUserPayload, parentId: UUID, context: IContext?): User? =
        database.suspendedTransaction {
            Users.insert {
                it[associationId] = parentId
                it[email] = payload.email
                it[password] = payload.password
                it[firstName] = payload.firstName
                it[lastName] = payload.lastName
                it[superuser] = payload.superuser
                it[lastLoginAt] = Clock.System.now()
            }
        }.resultedValues?.map(Users::toUser)?.singleOrNull()

    override suspend fun get(id: UUID): User? =
        database.suspendedTransaction {
            Users
                .selectAll()
                .where { Users.id eq id }
                .map(Users::toUser)
                .singleOrNull()
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): User? =
        database.suspendedTransaction {
            Users
                .selectAll()
                .where { Users.id eq id and (Users.associationId eq parentId) }
                .map(Users::toUser)
                .singleOrNull()
        }

    override suspend fun getForEmail(
        email: String,
        includePassword: Boolean,
    ): User? =
        database.suspendedTransaction {
            Users
                .selectAll()
                .where { Users.email eq email }
                .map {
                    Users.toUser(it, includePassword)
                }
                .singleOrNull()
        }

    override suspend fun list(parentId: UUID, context: IContext?): List<User> =
        database.suspendedTransaction {
            Users
                .selectAll()
                .where { Users.associationId eq parentId }
                .map(Users::toUser)
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<User> =
        database.suspendedTransaction {
            Users
                .selectAll()
                .where { Users.associationId eq parentId }
                .andWhere(pagination.options)
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(Users::toUser)
        }


    override suspend fun update(id: UUID, payload: UpdateUserPayload, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Users.update({ Users.id eq id and (Users.associationId eq parentId) }) {
                payload.firstName?.let { firstName ->
                    it[Users.firstName] = firstName
                }
                payload.lastName?.let { lastName ->
                    it[Users.lastName] = lastName
                }
                payload.password?.let { password ->
                    it[Users.password] = password
                }
            }
        } == 1

    override suspend fun updateLastLogin(id: UUID): Boolean =
        database.suspendedTransaction {
            Users.update({ Users.id eq id }) {
                it[lastLoginAt] = Clock.System.now()
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Users.deleteWhere {
                Users.id eq id and (associationId eq parentId)
            }
        } == 1

    private fun Query.andWhere(options: IPaginationOptions?): Query = when (options) {
        is SearchOptions -> {
            val likeString = options.search
                .replace("%", "\\%")
                .replace("_", "\\_")
                .let { "%$it%" }
            andWhere { Users.firstName like likeString or (Users.lastName like likeString) or (Users.email like likeString) }
        }

        else -> this
    }

}
