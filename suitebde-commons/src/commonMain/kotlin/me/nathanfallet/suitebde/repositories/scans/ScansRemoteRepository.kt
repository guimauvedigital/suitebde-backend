package me.nathanfallet.suitebde.repositories.scans

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
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

}
