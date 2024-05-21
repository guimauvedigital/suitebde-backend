package me.nathanfallet.suitebde.repositories.events

import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IEventsRepository :
    IChildModelSuspendRepository<Event, String, CreateEventPayload, UpdateEventPayload, String>
