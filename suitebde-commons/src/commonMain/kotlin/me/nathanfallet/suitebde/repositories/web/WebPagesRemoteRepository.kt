package me.nathanfallet.suitebde.repositories.web

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.usecases.models.id.RecursiveId

class WebPagesRemoteRepository(
    client: ISuiteBDEClient,
) : APIChildModelRemoteRepository<WebPage, String, WebPagePayload, WebPagePayload, String>(
    typeInfo<WebPage>(),
    typeInfo<WebPagePayload>(),
    typeInfo<WebPagePayload>(),
    typeInfo<List<WebPage>>(),
    client,
    null,
    prefix = "/api/v1"
), IWebPagesRemoteRepository {

    override suspend fun list(): List<WebPage> {
        return list(RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun get(id: String): WebPage? {
        return get(id, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun create(payload: WebPagePayload): WebPage? {
        return create(payload, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun update(id: String, payload: WebPagePayload): WebPage? {
        return update(id, payload, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun delete(id: String): Boolean {
        return delete(id, RecursiveId<Association, String, Unit>(""), null)
    }

}
