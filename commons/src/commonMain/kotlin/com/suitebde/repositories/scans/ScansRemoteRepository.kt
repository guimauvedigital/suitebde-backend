package com.suitebde.repositories.scans

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.models.scans.ScansForDay
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.datetime.LocalDate

class ScansRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<Scan, UUID, CreateScanPayload, Unit, UUID>(
    typeInfo<Scan>(),
    typeInfo<CreateScanPayload>(),
    typeInfo<Unit>(),
    typeInfo<List<Scan>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IScansRemoteRepository {

    override suspend fun create(payload: CreateScanPayload, associationId: UUID): Scan? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun list(startsAt: LocalDate, endsAt: LocalDate, associationId: UUID): List<ScansForDay> =
        client
            .request(HttpMethod.Get, constructFullRoute(RecursiveId<Association, UUID, Unit>(associationId))) {
                parameter("startsAt", startsAt)
                parameter("endsAt", endsAt)
            }
            .body()

}
