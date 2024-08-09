package com.suitebde.repositories.users

import com.suitebde.models.users.ResetInUser
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant

interface IResetsInUsersRepository {

    suspend fun create(userId: UUID, expiration: Instant): ResetInUser?
    suspend fun get(code: String): ResetInUser?
    suspend fun delete(code: String): Boolean
    suspend fun getExpiringBefore(date: Instant): List<ResetInUser>

}
