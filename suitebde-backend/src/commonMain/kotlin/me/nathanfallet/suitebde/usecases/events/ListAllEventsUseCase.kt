package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.repositories.events.IEventsRepository

class ListAllEventsUseCase(
    private val repository: IEventsRepository,
) : IListAllEventsUseCase {

    override suspend fun invoke(input: String): List<Event> = repository.listAll(input)

}
