package com.suitebde.repositories.associations

import com.suitebde.models.associations.CodeInEmail
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant

interface ICodesInEmailsRepository {

    suspend fun getCodeInEmail(code: String): CodeInEmail?
    suspend fun getCodesInEmailsExpiringBefore(date: Instant): List<CodeInEmail>
    suspend fun createCodeInEmail(email: String, code: String, associationId: UUID?, expiresAt: Instant): CodeInEmail?
    suspend fun updateCodeInEmail(email: String, code: String, associationId: UUID?, expiresAt: Instant): Boolean
    suspend fun deleteCodeInEmail(code: String)

}
