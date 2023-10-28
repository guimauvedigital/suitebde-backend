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

    suspend fun getUser(id: String): User?
    suspend fun getUserForEmail(email: String, includePassword: Boolean): User?
    suspend fun getUsersInAssociation(associationId: String): List<User>

    suspend fun updateUser(user: User): Int

}