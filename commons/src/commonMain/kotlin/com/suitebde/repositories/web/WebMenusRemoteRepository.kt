package com.suitebde.repositories.web

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class WebMenusRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<WebMenu, UUID, CreateWebMenuPayload, UpdateWebMenuPayload, UUID>(
    typeInfo<WebMenu>(),
    typeInfo<CreateWebMenuPayload>(),
    typeInfo<UpdateWebMenuPayload>(),
    typeInfo<List<WebMenu>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IWebMenusRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: UUID): List<WebMenu> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): WebMenu? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(payload: CreateWebMenuPayload, associationId: UUID): WebMenu? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(id: UUID, payload: UpdateWebMenuPayload, associationId: UUID): WebMenu? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

}
