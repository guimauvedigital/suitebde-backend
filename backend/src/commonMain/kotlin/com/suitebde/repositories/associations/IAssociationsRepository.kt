package com.suitebde.repositories.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IModelSuspendRepository
import kotlinx.datetime.Instant

interface IAssociationsRepository :
    IModelSuspendRepository<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload> {

    suspend fun updateExpiresAt(id: UUID, expiresAt: Instant): Boolean
    suspend fun getValidatedAssociations(): List<Association>
    suspend fun getAssociationsExpiringBefore(date: Instant): List<Association>
    suspend fun getAssociationForDomain(domain: String): Association?

}
