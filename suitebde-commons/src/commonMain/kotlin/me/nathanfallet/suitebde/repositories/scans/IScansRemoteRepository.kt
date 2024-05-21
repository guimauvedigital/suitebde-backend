package me.nathanfallet.suitebde.repositories.scans

import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan

interface IScansRemoteRepository {

    suspend fun create(payload: CreateScanPayload, associationId: String): Scan?

}
