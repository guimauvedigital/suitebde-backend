package me.nathanfallet.suitebde.repositories

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.associations.DomainInAssociation

interface IAssociationsRepository {

    suspend fun createAssociation(
        name: String,
        school: String,
        city: String,
        validated: Boolean,
        createdAt: Instant,
        expiresAt: Instant
    ): Association?

    suspend fun updateAssociation(association: Association): Int
    suspend fun deleteAssociation(association: Association)
    suspend fun getAssociation(id: String): Association?
    suspend fun getAssociations(): List<Association>
    suspend fun getValidatedAssociations(): List<Association>
    suspend fun getAssociationForDomain(domain: String): Association?

    suspend fun createDomain(domain: String, associationId: String): DomainInAssociation?
    suspend fun deleteDomain(domain: String)
    suspend fun getDomains(associationId: String): List<DomainInAssociation>

    suspend fun getCodeInEmail(code: String): CodeInEmail?
    suspend fun createCodeInEmail(email: String, code: String, associationId: String?, expiresAt: Instant): CodeInEmail?
    suspend fun updateCodeInEmail(email: String, code: String, associationId: String?, expiresAt: Instant): Int
    suspend fun deleteCodeInEmail(code: String)
    suspend fun deleteCodeInEmailBefore(date: Instant)

}