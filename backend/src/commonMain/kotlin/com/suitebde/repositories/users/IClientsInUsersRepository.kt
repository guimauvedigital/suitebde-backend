package com.suitebde.repositories.users

import com.suitebde.models.users.ClientInUser
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant

interface IClientsInUsersRepository {

    suspend fun create(userId: UUID, clientId: UUID, expiration: Instant): ClientInUser?
    suspend fun get(code: String): ClientInUser?
    suspend fun delete(code: String): Boolean
    suspend fun getExpiringBefore(date: Instant): List<ClientInUser>

}
