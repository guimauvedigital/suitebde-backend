package me.nathanfallet.suitebde.repositories.clubs

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateUserInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.models.id.RecursiveId

class UsersInClubsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Club, String, *, *, *>,
) : APIChildModelRemoteRepository<UserInClub, String, CreateUserInClub, Unit, String>(
    typeInfo<UserInClub>(),
    typeInfo<CreateUserInClub>(),
    typeInfo<Unit>(),
    typeInfo<List<UserInClub>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IUsersInClubsRemoteRepository {

    override suspend fun list(limit: Long, offset: Long, clubId: String, associationId: String): List<UserInClub> {
        return list(
            limit,
            offset,
            RecursiveId<Club, String, String>(clubId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )
    }

    override suspend fun get(userId: String, clubId: String, associationId: String): UserInClub? {
        return get(
            userId,
            RecursiveId<Club, String, String>(clubId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )
    }

    override suspend fun create(payload: CreateUserInClub, clubId: String, associationId: String): UserInClub? {
        return create(
            payload,
            RecursiveId<Club, String, String>(clubId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )
    }

    override suspend fun delete(userId: String, clubId: String, associationId: String): Boolean {
        return delete(
            userId,
            RecursiveId<Club, String, String>(clubId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )
    }

}
