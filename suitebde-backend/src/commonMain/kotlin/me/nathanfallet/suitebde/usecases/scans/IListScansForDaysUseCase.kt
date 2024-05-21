package me.nathanfallet.suitebde.usecases.scans

import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.usecases.base.ITripleSuspendUseCase

interface IListScansForDaysUseCase : ITripleSuspendUseCase<String, LocalDate, LocalDate, List<ScansForDay>>
