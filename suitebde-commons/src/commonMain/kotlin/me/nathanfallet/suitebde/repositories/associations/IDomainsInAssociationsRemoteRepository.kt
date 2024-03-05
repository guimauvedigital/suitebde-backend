package me.nathanfallet.suitebde.repositories.associations

import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.usecases.pagination.Pagination

interface IDomainsInAssociationsRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: String): List<DomainInAssociation>
    suspend fun create(payload: CreateDomainInAssociationPayload, associationId: String): DomainInAssociation?
    suspend fun delete(domain: String, associationId: String): Boolean

}
