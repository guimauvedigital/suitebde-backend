package me.nathanfallet.suitebde.repositories.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail

interface ICodesInEmailsRepository {

    suspend fun getCodeInEmail(code: String): CodeInEmail?
    suspend fun getCodesInEmailsExpiringBefore(date: Instant): List<CodeInEmail>
    suspend fun createCodeInEmail(email: String, code: String, associationId: String?, expiresAt: Instant): CodeInEmail?
    suspend fun updateCodeInEmail(email: String, code: String, associationId: String?, expiresAt: Instant): Int
    suspend fun deleteCodeInEmail(code: String)

}
