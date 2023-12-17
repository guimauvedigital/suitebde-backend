package me.nathanfallet.suitebde.repositories.web

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.models.id.RecursiveId

class WebMenusRemoteRepository(
    client: ISuiteBDEClient,
) : APIChildModelRemoteRepository<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, String>(
    typeInfo<WebMenu>(),
    typeInfo<CreateWebMenuPayload>(),
    typeInfo<UpdateWebMenuPayload>(),
    typeInfo<List<WebMenu>>(),
    client,
    null,
    prefix = "/api/v1"
), IWebMenusRemoteRepository {

    override suspend fun list(): List<WebMenu> {
        return list(RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun get(id: String): WebMenu? {
        return get(id, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun create(payload: CreateWebMenuPayload): WebMenu? {
        return create(payload, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun update(id: String, payload: UpdateWebMenuPayload): WebMenu? {
        return update(id, payload, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun delete(id: String): Boolean {
        return delete(id, RecursiveId<Association, String, Unit>(""), null)
    }

}
