package me.nathanfallet.suitebde.database.users

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*

class DatabaseUsersRepository(
    private val database: Database,
) : IUsersRepository {

    override suspend fun create(payload: CreateUserPayload, parentId: String, context: IContext?): User? {
        return database.dbQuery {
            Users.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[email] = payload.email
                it[password] = payload.password
                it[firstName] = payload.firstName
                it[lastName] = payload.lastName
                it[superuser] = payload.superuser
            }
        }.resultedValues?.map(Users::toUser)?.singleOrNull()
    }

    override suspend fun get(id: String): User? {
        return database.dbQuery {
            Users
                .select { Users.id eq id }
                .map(Users::toUser)
                .singleOrNull()
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): User? {
        return database.dbQuery {
            Users
                .select { Users.id eq id and (Users.associationId eq parentId) }
                .map(Users::toUser)
                .singleOrNull()
        }
    }

    override suspend fun getForEmail(
        email: String,
        includePassword: Boolean,
    ): User? {
        return database.dbQuery {
            Users
                .select { Users.email eq email }
                .map {
                    Users.toUser(it, includePassword)
                }
                .singleOrNull()
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<User> {
        return database.dbQuery {
            Users
                .select { Users.associationId eq parentId }
                .map(Users::toUser)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<User> {
        return database.dbQuery {
            Users
                .select { Users.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(Users::toUser)
        }
    }

    override suspend fun update(id: String, payload: UpdateUserPayload, parentId: String, context: IContext?): Boolean {
        return database.dbQuery {
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
    }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.dbQuery {
            Users.deleteWhere {
                Op.build { Users.id eq id and (associationId eq parentId) }
            }
        } == 1
    }

}
