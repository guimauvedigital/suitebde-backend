package me.nathanfallet.suitebde.repositories.web

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.usecases.models.id.RecursiveId

class WebPagesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<WebPage, String, WebPagePayload, WebPagePayload, String>(
    typeInfo<WebPage>(),
    typeInfo<WebPagePayload>(),
    typeInfo<WebPagePayload>(),
    typeInfo<List<WebPage>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IWebPagesRemoteRepository {

    override suspend fun list(limit: Long, offset: Long, associationId: String): List<WebPage> =
        list(limit, offset, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun get(id: String, associationId: String): WebPage? =
        get(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(payload: WebPagePayload, associationId: String): WebPage? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun update(id: String, payload: WebPagePayload, associationId: String): WebPage? =
        update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(id: String, associationId: String): Boolean =
        delete(id, RecursiveId<Association, String, Unit>(associationId), null)

}
