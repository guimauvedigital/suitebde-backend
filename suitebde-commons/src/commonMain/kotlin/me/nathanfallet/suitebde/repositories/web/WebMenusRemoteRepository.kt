package me.nathanfallet.suitebde.repositories.web

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.models.id.RecursiveId

class WebMenusRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, String>(
    typeInfo<WebMenu>(),
    typeInfo<CreateWebMenuPayload>(),
    typeInfo<UpdateWebMenuPayload>(),
    typeInfo<List<WebMenu>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IWebMenusRemoteRepository {

    override suspend fun list(limit: Long, offset: Long, associationId: String): List<WebMenu> =
        list(limit, offset, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun get(id: String, associationId: String): WebMenu? =
        get(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(payload: CreateWebMenuPayload, associationId: String): WebMenu? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun update(id: String, payload: UpdateWebMenuPayload, associationId: String): WebMenu? =
        update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(id: String, associationId: String): Boolean =
        delete(id, RecursiveId<Association, String, Unit>(associationId), null)

}
