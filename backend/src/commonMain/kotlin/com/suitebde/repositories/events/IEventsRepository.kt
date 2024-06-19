package com.suitebde.repositories.events

import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository
import kotlinx.datetime.LocalDate

interface IEventsRepository : IChildModelSuspendRepository<Event, UUID, CreateEventPayload, UpdateEventPayload, UUID> {

    suspend fun listAll(associationId: UUID): List<Event>
    suspend fun listBetween(associationId: UUID, startsAt: LocalDate, endsAt: LocalDate): List<Event>

}
