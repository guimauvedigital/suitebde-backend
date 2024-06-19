package com.suitebde.repositories.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class EventsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<Event, UUID, CreateEventPayload, UpdateEventPayload, UUID>(
    typeInfo<Event>(),
    typeInfo<CreateEventPayload>(),
    typeInfo<UpdateEventPayload>(),
    typeInfo<List<Event>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IEventsRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: UUID): List<Event> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): Event? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(payload: CreateEventPayload, associationId: UUID): Event? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(id: UUID, payload: UpdateEventPayload, associationId: UUID): Event? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

}
