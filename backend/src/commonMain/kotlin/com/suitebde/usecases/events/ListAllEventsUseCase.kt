package com.suitebde.usecases.events

import com.suitebde.models.events.Event
import com.suitebde.repositories.events.IEventsRepository
import dev.kaccelero.models.UUID

class ListAllEventsUseCase(
    private val repository: IEventsRepository,
) : IListAllEventsUseCase {

    override suspend fun invoke(input: UUID): List<Event> = repository.listAll(input)

}
