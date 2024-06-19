package com.suitebde.repositories.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIModelRemoteRepository
import io.ktor.util.reflect.*

class AssociationsRemoteRepository(
    client: ISuiteBDEClient,
) : APIModelRemoteRepository<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    typeInfo<List<Association>>(),
    client,
    prefix = "/api/v1"
), IAssociationsRemoteRepository {

    override suspend fun list(): List<Association> =
        list(null)

    override suspend fun get(id: UUID): Association? =
        get(id, null)

    override suspend fun update(id: UUID, payload: UpdateAssociationPayload): Association? =
        update(id, payload, null)

}
