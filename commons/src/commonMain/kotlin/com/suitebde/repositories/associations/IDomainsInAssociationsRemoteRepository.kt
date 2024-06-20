package com.suitebde.repositories.associations

import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IDomainsInAssociationsRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<DomainInAssociation>
    suspend fun create(payload: CreateDomainInAssociationPayload, associationId: UUID): DomainInAssociation?
    suspend fun delete(domain: String, associationId: UUID): Boolean

}
