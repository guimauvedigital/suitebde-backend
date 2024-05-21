package me.nathanfallet.suitebde.usecases.scans

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.repositories.scans.IScansRepository

class ListScansForDaysUseCase(
    private val scansRepository: IScansRepository,
    private val eventsRepository: IEventsRepository,
) : IListScansForDaysUseCase {

    override suspend fun invoke(input1: String, input2: LocalDate, input3: LocalDate): List<ScansForDay> {
        val timeZone = TimeZone.currentSystemDefault()
        val events = eventsRepository.listBetween(input1, input2, input3)
        return scansRepository.listBetween(input1, input2, input3)
            .groupBy { it.scannedAt.toLocalDateTime(timeZone).date }
            .map { scan ->
                ScansForDay(
                    scan.key,
                    scan.value,
                    events.filter {
                        scan.key >= it.startsAt.toLocalDateTime(timeZone).date &&
                                scan.key <= it.endsAt.toLocalDateTime(timeZone).date
                    }
                )
            }
            .sortedByDescending { it.date }
    }

}
