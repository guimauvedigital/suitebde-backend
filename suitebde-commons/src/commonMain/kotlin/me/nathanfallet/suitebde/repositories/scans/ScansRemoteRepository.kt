package me.nathanfallet.suitebde.repositories.scans

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.datetime.LocalDate
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.usecases.models.id.RecursiveId

class ScansRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<Scan, String, CreateScanPayload, Unit, String>(
    typeInfo<Scan>(),
    typeInfo<CreateScanPayload>(),
    typeInfo<Unit>(),
    typeInfo<List<Scan>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IScansRemoteRepository {

    override suspend fun create(payload: CreateScanPayload, associationId: String): Scan? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun list(startsAt: LocalDate, endsAt: LocalDate, associationId: String): List<ScansForDay> =
        client
            .request(HttpMethod.Get, constructFullRoute(RecursiveId<Association, String, Unit>(associationId))) {
                parameter("startsAt", startsAt)
                parameter("endsAt", endsAt)
            }
            .body()

}
