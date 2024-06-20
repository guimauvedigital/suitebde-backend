package com.suitebde.repositories.events

import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IEventsRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<Event>
    suspend fun get(id: UUID, associationId: UUID): Event?
    suspend fun create(payload: CreateEventPayload, associationId: UUID): Event?
    suspend fun update(id: UUID, payload: UpdateEventPayload, associationId: UUID): Event?
    suspend fun delete(id: UUID, associationId: UUID): Boolean

}
