package me.nathanfallet.suitebde.repositories.events

import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IEventsRepository :
    IChildModelSuspendRepository<Event, String, CreateEventPayload, UpdateEventPayload, String> {

    suspend fun listAll(associationId: String): List<Event>
    suspend fun listBetween(parentId: String, startsAt: LocalDate, endsAt: LocalDate): List<Event>

}
