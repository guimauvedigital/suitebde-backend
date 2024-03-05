package me.nathanfallet.suitebde.repositories.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateUserInRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.usecases.models.id.RecursiveId
import me.nathanfallet.usecases.pagination.Pagination

class UsersInRolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Role, String, *, *, *>,
) : APIChildModelRemoteRepository<UserInRole, String, CreateUserInRolePayload, Unit, String>(
    typeInfo<UserInRole>(),
    typeInfo<CreateUserInRolePayload>(),
    typeInfo<Unit>(),
    typeInfo<List<UserInRole>>(),
    client,
    parentRepository,
    prefix = "/api/v1",
    route = "users"
), IUsersInRolesRemoteRepository {

    override suspend fun list(pagination: Pagination, roleId: String, associationId: String): List<UserInRole> =
        list(
            pagination,
            RecursiveId<Role, String, String>(roleId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun create(payload: CreateUserInRolePayload, roleId: String, associationId: String): UserInRole? =
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
