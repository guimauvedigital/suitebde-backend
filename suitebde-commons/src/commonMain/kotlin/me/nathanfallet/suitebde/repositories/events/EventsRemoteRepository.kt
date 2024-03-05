package me.nathanfallet.suitebde.repositories.events

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.usecases.models.id.RecursiveId
import me.nathanfallet.usecases.pagination.Pagination

class EventsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<Event, String, CreateEventPayload, UpdateEventPayload, String>(
    typeInfo<Event>(),
    typeInfo<CreateEventPayload>(),
    typeInfo<UpdateEventPayload>(),
    typeInfo<List<Event>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IEventsRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: String): List<Event> =
        list(pagination, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun get(id: String, associationId: String): Event? =
        get(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(payload: CreateEventPayload, associationId: String): Event? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun update(id: String, payload: UpdateEventPayload, associationId: String): Event? =
        update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(id: String, associationId: String): Boolean =
        delete(id, RecursiveId<Association, String, Unit>(associationId), null)

}
