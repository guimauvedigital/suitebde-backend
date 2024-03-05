package me.nathanfallet.suitebde.repositories.associations

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.usecases.models.id.RecursiveId
import me.nathanfallet.usecases.pagination.Pagination

class DomainsInAssociationsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, String>(
    typeInfo<DomainInAssociation>(),
    typeInfo<CreateDomainInAssociationPayload>(),
    typeInfo<Unit>(),
    typeInfo<List<DomainInAssociation>>(),
    client,
    parentRepository,
    route = "domains",
    prefix = "/api/v1"
), IDomainsInAssociationsRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: String): List<DomainInAssociation> =
        list(pagination, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(
        payload: CreateDomainInAssociationPayload,
        associationId: String,
    ): DomainInAssociation? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(domain: String, associationId: String): Boolean =
        delete(domain, RecursiveId<Association, String, Unit>(associationId), null)

}
