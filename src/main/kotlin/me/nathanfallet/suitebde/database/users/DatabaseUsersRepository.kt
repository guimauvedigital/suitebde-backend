package me.nathanfallet.suitebde.database.users

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class DatabaseUsersRepository(
    private val database: Database
) : IUsersRepository {

    override suspend fun createUser(
        associationId: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        superuser: Boolean
    ): User? {
        return database.dbQuery {
            Users.insert {
                it[id] = generateId()
                it[this.associationId] = associationId
                it[this.email] = email
                it[this.password] = password
                it[this.firstName] = firstName
                it[this.lastName] = lastName
                it[this.superuser] = superuser
            }
        }.resultedValues?.map(Users::toUser)?.singleOrNull()
    }

    override suspend fun getUser(id: String): User? {
        return database.dbQuery {
            Users
                .select { Users.id eq id }
                .map(Users::toUser)
                .singleOrNull()
        }
    }

    override suspend fun getUserForEmail(
        email: String,
        includePassword: Boolean
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

    override suspend fun getUsersInAssociation(associationId: String): List<User> {
        return database.dbQuery {
            Users
                .select { Users.associationId eq associationId }
                .map(Users::toUser)
        }
    }

    override suspend fun updateUser(user: User): Int {
        return database.dbQuery {
            Users.update({ Users.id eq user.id }) {
                it[this.email] = user.email
                it[this.firstName] = user.firstName
                it[this.lastName] = user.lastName
                user.password?.let { password ->
                    it[this.password] = password
                }
            }
        }
    }

}