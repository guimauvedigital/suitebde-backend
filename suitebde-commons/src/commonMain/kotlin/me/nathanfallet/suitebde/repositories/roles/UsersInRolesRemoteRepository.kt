package me.nathanfallet.suitebde.repositories.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateUserInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.usecases.models.id.RecursiveId

class UsersInRolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Role, String, *, *, *>,
) : APIChildModelRemoteRepository<UserInRole, String, CreateUserInRole, Unit, String>(
    typeInfo<UserInRole>(),
    typeInfo<CreateUserInRole>(),
    typeInfo<Unit>(),
    typeInfo<List<UserInRole>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IUsersInRolesRemoteRepository {

    override suspend fun list(limit: Long, offset: Long, roleId: String, associationId: String): List<UserInRole> =
        list(
            limit,
            offset,
            RecursiveId<Role, String, String>(roleId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun get(userId: String, roleId: String, associationId: String): UserInRole? =
        get(
            userId,
            RecursiveId<Role, String, String>(roleId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun create(payload: CreateUserInRole, roleId: String, associationId: String): UserInRole? =
        create(
            payload,
            RecursiveId<Role, String, String>(roleId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun delete(userId: String, roleId: String, associationId: String): Boolean =
        delete(
            userId,
            RecursiveId<Role, String, String>(roleId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

}
