package me.nathanfallet.suitebde.database.users

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import org.jetbrains.exposed.sql.insert

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

}