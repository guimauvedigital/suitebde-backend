package com.suitebde.usecases.events

import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface IListAllEventsUseCase : ISuspendUseCase<UUID, List<Event>>
