package me.nathanfallet.suitebde.repositories.scans

import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.models.scans.ScansForDay

interface IScansRemoteRepository {

    suspend fun create(payload: CreateScanPayload, associationId: String): Scan?
    suspend fun list(startsAt: LocalDate, endsAt: LocalDate, associationId: String): List<ScansForDay>

}
