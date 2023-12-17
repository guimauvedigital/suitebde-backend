package me.nathanfallet.suitebde.repositories.users

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.id.RecursiveId

class UsersRemoteRepository(
    client: ISuiteBDEClient,
) : APIChildModelRemoteRepository<User, String, CreateUserPayload, UpdateUserPayload, String>(
    typeInfo<User>(),
    typeInfo<CreateUserPayload>(),
    typeInfo<UpdateUserPayload>(),
    typeInfo<List<User>>(),
    client,
    null,
    prefix = "/api/v1"
), IUsersRemoteRepository {

    override suspend fun list(): List<User> {
        return list(RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun get(id: String): User? {
        return get(id, RecursiveId<Association, String, Unit>(""), null)
    }

    override suspend fun update(id: String, payload: UpdateUserPayload): User? {
        return update(id, payload, RecursiveId<Association, String, Unit>(""), null)
    }

}
