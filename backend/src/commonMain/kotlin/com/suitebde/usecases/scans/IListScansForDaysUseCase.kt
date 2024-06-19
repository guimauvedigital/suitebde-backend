package com.suitebde.usecases.scans

import com.suitebde.models.scans.ScansForDay
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ITripleSuspendUseCase
import kotlinx.datetime.LocalDate

interface IListScansForDaysUseCase : ITripleSuspendUseCase<UUID, LocalDate, LocalDate, List<ScansForDay>>
