package com.suitebde.repositories.scans

import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.models.scans.ScansForDay
import dev.kaccelero.models.UUID
import kotlinx.datetime.LocalDate

interface IScansRemoteRepository {

    suspend fun create(payload: CreateScanPayload, associationId: UUID): Scan?
    suspend fun list(startsAt: LocalDate, endsAt: LocalDate, associationId: UUID): List<ScansForDay>

}
