package me.nathanfallet.suitebde.repositories.clubs

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.usecases.models.id.RecursiveId

class ClubsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<Club, String, CreateClubPayload, UpdateClubPayload, String>(
    typeInfo<Club>(),
    typeInfo<CreateClubPayload>(),
    typeInfo<UpdateClubPayload>(),
    typeInfo<List<Club>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IClubsRemoteRepository {

    override suspend fun list(limit: Long, offset: Long, associationId: String): List<Club> =
        list(limit, offset, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun get(id: String, associationId: String): Club? =
        get(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(payload: CreateClubPayload, associationId: String): Club? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun update(id: String, payload: UpdateClubPayload, associationId: String): Club? =
        update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(id: String, associationId: String): Boolean =
        delete(id, RecursiveId<Association, String, Unit>(associationId), null)

}
