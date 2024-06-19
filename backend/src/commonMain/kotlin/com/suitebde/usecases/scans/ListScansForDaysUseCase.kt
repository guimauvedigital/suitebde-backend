package com.suitebde.usecases.scans

import com.suitebde.models.scans.ScansForDay
import com.suitebde.repositories.events.IEventsRepository
import com.suitebde.repositories.scans.IScansRepository
import dev.kaccelero.models.UUID
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ListScansForDaysUseCase(
    private val scansRepository: IScansRepository,
    private val eventsRepository: IEventsRepository,
) : IListScansForDaysUseCase {

    override suspend fun invoke(input1: UUID, input2: LocalDate, input3: LocalDate): List<ScansForDay> {
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
