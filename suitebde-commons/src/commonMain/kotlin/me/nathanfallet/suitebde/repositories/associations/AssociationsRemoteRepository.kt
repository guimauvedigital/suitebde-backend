package me.nathanfallet.suitebde.repositories.associations

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

class AssociationsRemoteRepository(
    client: ISuiteBDEClient,
) : APIModelRemoteRepository<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    typeInfo<List<Association>>(),
    client,
    prefix = "/api/v1"
), IAssociationsRemoteRepository {

    override suspend fun list(): List<Association> =
        list(null)

    override suspend fun get(id: String): Association? =
        get(id, null)

    override suspend fun update(id: String, payload: UpdateAssociationPayload): Association? =
        update(id, payload, null)

}
