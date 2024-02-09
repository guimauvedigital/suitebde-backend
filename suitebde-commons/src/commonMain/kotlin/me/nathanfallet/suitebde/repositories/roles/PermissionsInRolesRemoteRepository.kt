package me.nathanfallet.suitebde.repositories.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreatePermissionInRolePayload
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.usecases.models.id.RecursiveId

class PermissionsInRolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<Role, String, *, *, *>,
) : APIChildModelRemoteRepository<PermissionInRole, String, CreatePermissionInRolePayload, Unit, String>(
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
        limit: Long,
        offset: Long,
        roleId: String,
        associationId: String,
    ): List<PermissionInRole> =
        list(
            limit,
            offset,
            RecursiveId<Role, String, String>(roleId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun create(
        payload: CreatePermissionInRolePayload,
        roleId: String,
        associationId: String,
    ): PermissionInRole? =
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
