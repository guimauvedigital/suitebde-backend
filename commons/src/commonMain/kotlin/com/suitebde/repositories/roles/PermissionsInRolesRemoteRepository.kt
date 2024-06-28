package com.suitebde.repositories.roles

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreatePermissionInRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.PermissionInRole
import com.suitebde.models.roles.Role
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIChildModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class PermissionsInRolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Role, UUID, *, *, *>,
) : APIChildModelRemoteRepository<PermissionInRole, Permission, CreatePermissionInRolePayload, Unit, UUID>(
    typeInfo<PermissionInRole>(),
    typeInfo<CreatePermissionInRolePayload>(),
    typeInfo<Unit>(),
    typeInfo<List<PermissionInRole>>(),
    client,
    parentRepository,
    prefix = "/api/v1",
    route = "permissions"
), IPermissionsInRolesRemoteRepository {

    override suspend fun list(
        pagination: Pagination,
        roleId: UUID,
        associationId: UUID,
    ): List<PermissionInRole> =
        list(
            pagination,
            RecursiveId<Role, UUID, UUID>(roleId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun create(
        payload: CreatePermissionInRolePayload,
        roleId: UUID,
        associationId: UUID,
    ): PermissionInRole? =
        create(
            payload,
            RecursiveId<Role, UUID, UUID>(roleId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun delete(permission: Permission, roleId: UUID, associationId: UUID): Boolean =
        delete(
            permission,
            RecursiveId<Role, UUID, UUID>(roleId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

}
