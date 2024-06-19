package com.suitebde.repositories.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.UserInClub
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIChildModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class UsersInClubsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Club, UUID, *, *, *>,
) : APIChildModelRemoteRepository<UserInClub, UUID, CreateUserInClubPayload, Unit, UUID>(
    typeInfo<UserInClub>(),
    typeInfo<CreateUserInClubPayload>(),
    typeInfo<Unit>(),
    typeInfo<List<UserInClub>>(),
    client,
    parentRepository,
    route = "users",
    prefix = "/api/v1"
), IUsersInClubsRemoteRepository {

    override suspend fun list(pagination: Pagination, clubId: UUID, associationId: UUID): List<UserInClub> =
        list(
            pagination,
            RecursiveId<Club, UUID, UUID>(clubId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun create(payload: CreateUserInClubPayload, clubId: UUID, associationId: UUID): UserInClub? =
        create(
            payload,
            RecursiveId<Club, UUID, UUID>(clubId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun delete(userId: UUID, clubId: UUID, associationId: UUID): Boolean =
        delete(
            userId,
            RecursiveId<Club, UUID, UUID>(clubId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

}
