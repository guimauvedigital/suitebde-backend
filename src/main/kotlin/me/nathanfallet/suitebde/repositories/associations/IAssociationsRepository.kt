package me.nathanfallet.suitebde.repositories.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.*
import me.nathanfallet.usecases.models.repositories.IModelSuspendRepository

interface IAssociationsRepository :
    IModelSuspendRepository<Association, String, CreateAssociationPayload, UpdateAssociationPayload> {

    suspend fun updateExpiresAt(id: String, expiresAt: Instant): Boolean
    suspend fun getAssociations(): List<Association>
    suspend fun getValidatedAssociations(): List<Association>
    suspend fun getAssociationsExpiringBefore(date: Instant): List<Association>
    suspend fun getAssociationForDomain(domain: String): Association?

    suspend fun createDomain(domain: String, associationId: String): DomainInAssociation?
    suspend fun deleteDomain(domain: String)
    suspend fun getDomains(associationId: String): List<DomainInAssociation>

    suspend fun getCodeInEmail(code: String): CodeInEmail?
    suspend fun getCodesInEmailsExpiringBefore(date: Instant): List<CodeInEmail>
    suspend fun createCodeInEmail(email: String, code: String, associationId: String?, expiresAt: Instant): CodeInEmail?
    suspend fun updateCodeInEmail(email: String, code: String, associationId: String?, expiresAt: Instant): Int
    suspend fun deleteCodeInEmail(code: String)

}