package com.suitebde.repositories.roles

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreateUserInRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UserInRole
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIChildModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class UsersInRolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Role, UUID, *, *, *>,
) : APIChildModelRemoteRepository<UserInRole, UUID, CreateUserInRolePayload, Unit, UUID>(
    typeInfo<UserInRole>(),
    typeInfo<CreateUserInRolePayload>(),
    typeInfo<Unit>(),
    typeInfo<List<UserInRole>>(),
    client,
    parentRepository,
    route = "users",
    prefix = "/api/v1",
), IUsersInRolesRemoteRepository {

    override suspend fun list(pagination: Pagination, roleId: UUID, associationId: UUID): List<UserInRole> =
        list(
            pagination,
            RecursiveId<Role, UUID, UUID>(roleId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun create(payload: CreateUserInRolePayload, roleId: UUID, associationId: UUID): UserInRole? =
        create(
            payload,
            RecursiveId<Role, UUID, UUID>(roleId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun delete(userId: UUID, roleId: UUID, associationId: UUID): Boolean =
        delete(
            userId,
            RecursiveId<Role, UUID, UUID>(roleId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

}
