package com.suitebde.repositories.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class DomainsInAssociationsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, UUID>(
    typeInfo<DomainInAssociation>(),
    typeInfo<CreateDomainInAssociationPayload>(),
    typeInfo<Unit>(),
    typeInfo<List<DomainInAssociation>>(),
    client,
    parentRepository,
    route = "domains",
    prefix = "/api/v1"
), IDomainsInAssociationsRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: UUID): List<DomainInAssociation> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(
        payload: CreateDomainInAssociationPayload,
        associationId: UUID,
    ): DomainInAssociation? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(domain: String, associationId: UUID): Boolean =
        delete(domain, RecursiveId<Association, UUID, Unit>(associationId), null)

}
