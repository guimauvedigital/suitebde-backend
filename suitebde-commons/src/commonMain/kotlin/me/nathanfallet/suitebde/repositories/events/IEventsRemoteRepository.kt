package me.nathanfallet.suitebde.repositories.events

import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload

interface IEventsRemoteRepository {

    suspend fun list(limit: Long, offset: Long, associationId: String): List<Event>
    suspend fun get(id: String, associationId: String): Event?
    suspend fun create(payload: CreateEventPayload, associationId: String): Event?
    suspend fun update(id: String, payload: UpdateEventPayload, associationId: String): Event?
    suspend fun delete(id: String, associationId: String): Boolean

}
