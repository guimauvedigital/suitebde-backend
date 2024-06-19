package com.suitebde.repositories.web

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class WebPagesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<WebPage, UUID, WebPagePayload, WebPagePayload, UUID>(
    typeInfo<WebPage>(),
    typeInfo<WebPagePayload>(),
    typeInfo<WebPagePayload>(),
    typeInfo<List<WebPage>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IWebPagesRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: UUID): List<WebPage> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): WebPage? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(payload: WebPagePayload, associationId: UUID): WebPage? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(id: UUID, payload: WebPagePayload, associationId: UUID): WebPage? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

}
