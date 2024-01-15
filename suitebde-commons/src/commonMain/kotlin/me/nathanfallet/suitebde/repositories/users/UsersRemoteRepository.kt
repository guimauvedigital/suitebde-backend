package me.nathanfallet.suitebde.repositories.users

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.id.RecursiveId

class UsersRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<User, String, CreateUserPayload, UpdateUserPayload, String>(
    typeInfo<User>(),
    typeInfo<CreateUserPayload>(),
    typeInfo<UpdateUserPayload>(),
    typeInfo<List<User>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IUsersRemoteRepository {

    override suspend fun list(associationId: String): List<User> {
        return list(RecursiveId<Association, String, Unit>(associationId), null)
    }

    override suspend fun get(id: String, associationId: String): User? {
        return get(id, RecursiveId<Association, String, Unit>(associationId), null)
    }

    override suspend fun update(id: String, payload: UpdateUserPayload, associationId: String): User? {
        return update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)
    }

}
