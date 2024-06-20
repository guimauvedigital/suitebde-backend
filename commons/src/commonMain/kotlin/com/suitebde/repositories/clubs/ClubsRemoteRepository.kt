package com.suitebde.repositories.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.application.SearchOptions
import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateClubPayload
import com.suitebde.models.clubs.UpdateClubPayload
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.IPaginationOptions
import dev.kaccelero.repositories.Pagination
import io.ktor.client.request.*
import io.ktor.util.reflect.*

class ClubsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<Club, UUID, CreateClubPayload, UpdateClubPayload, UUID>(
    typeInfo<Club>(),
    typeInfo<CreateClubPayload>(),
    typeInfo<UpdateClubPayload>(),
    typeInfo<List<Club>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IClubsRemoteRepository {

    override fun encodePaginationOptions(options: IPaginationOptions, builder: HttpRequestBuilder) = when (options) {
        is SearchOptions -> builder.parameter("search", options.search)
        else -> super.encodePaginationOptions(options, builder)
    }

    override suspend fun list(pagination: Pagination, associationId: UUID): List<Club> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): Club? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(payload: CreateClubPayload, associationId: UUID): Club? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(id: UUID, payload: UpdateClubPayload, associationId: UUID): Club? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

}
