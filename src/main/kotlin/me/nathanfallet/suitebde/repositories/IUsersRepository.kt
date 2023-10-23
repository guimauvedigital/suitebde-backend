package me.nathanfallet.suitebde.repositories

import me.nathanfallet.suitebde.models.users.User

interface IUsersRepository {

    suspend fun createUser(
        associationId: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        superuser: Boolean
    ): User?

}