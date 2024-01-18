package me.nathanfallet.suitebde.repositories.associations

import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation

interface IDomainsInAssociationsRemoteRepository {

    suspend fun list(limit: Long, offset: Long, associationId: String): List<DomainInAssociation>
    suspend fun get(domain: String, associationId: String): DomainInAssociation?
    suspend fun create(payload: CreateDomainInAssociationPayload, associationId: String): DomainInAssociation?
    suspend fun delete(domain: String, associationId: String): Boolean

}
