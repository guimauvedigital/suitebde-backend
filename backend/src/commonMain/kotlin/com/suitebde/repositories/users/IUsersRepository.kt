package com.suitebde.repositories.users

import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository
import kotlinx.datetime.Instant

interface IUsersRepository : IChildModelSuspendRepository<User, UUID, CreateUserPayload, UpdateUserPayload, UUID> {

    suspend fun get(id: UUID): User?
    suspend fun getForEmail(email: String, includePassword: Boolean): User?
    suspend fun updateLastLogin(id: UUID): Boolean
    suspend fun listLastLoggedBefore(date: Instant): List<User>

}
