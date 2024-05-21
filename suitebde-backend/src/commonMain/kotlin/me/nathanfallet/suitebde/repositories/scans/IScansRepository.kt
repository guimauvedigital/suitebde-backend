package me.nathanfallet.suitebde.repositories.scans

import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IScansRepository : IChildModelSuspendRepository<Scan, String, CreateScanPayload, Unit, String> {

    suspend fun listBetween(parentId: String, startsAt: LocalDate, endsAt: LocalDate): List<Scan>

}
