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

    override suspend fun list(limit: Long, offset: Long, associationId: String): List<Event> {
        return list(limit, offset, RecursiveId<Association, String, Unit>(associationId), null)
    }

    override suspend fun get(id: String, associationId: String): Event? {
        return get(id, RecursiveId<Association, String, Unit>(associationId), null)
    }

    override suspend fun create(payload: CreateEventPayload, associationId: String): Event? {
        return create(payload, RecursiveId<Association, String, Unit>(associationId), null)
    }

    override suspend fun update(id: String, payload: UpdateEventPayload, associationId: String): Event? {
        return update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)
    }

    override suspend fun delete(id: String, associationId: String): Boolean {
        return delete(id, RecursiveId<Association, String, Unit>(associationId), null)
    }

}
