package com.suitebde.repositories.scans

import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository
import kotlinx.datetime.LocalDate

interface IScansRepository : IChildModelSuspendRepository<Scan, UUID, CreateScanPayload, Unit, UUID> {

    suspend fun listBetween(parentId: UUID, startsAt: LocalDate, endsAt: LocalDate): List<Scan>

}
