package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IUsersRepository : IChildModelSuspendRepository<User, String, CreateUserPayload, UpdateUserPayload, String> {

    suspend fun get(id: String): User?
    suspend fun getForEmail(email: String, includePassword: Boolean): User?

    suspend fun getInAssociation(associationId: String): List<User>

}
