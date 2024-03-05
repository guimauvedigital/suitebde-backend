package me.nathanfallet.suitebde.repositories.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.usecases.models.id.RecursiveId
import me.nathanfallet.usecases.pagination.Pagination

class RolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<Role, String, CreateRolePayload, UpdateRolePayload, String>(
    typeInfo<Role>(),
    typeInfo<CreateRolePayload>(),
    typeInfo<UpdateRolePayload>(),
    typeInfo<List<Role>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IRolesRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: String): List<Role> =
        list(pagination, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun get(id: String, associationId: String): Role? =
        get(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(payload: CreateRolePayload, associationId: String): Role? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun update(id: String, payload: UpdateRolePayload, associationId: String): Role? =
        update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(id: String, associationId: String): Boolean =
        delete(id, RecursiveId<Association, String, Unit>(associationId), null)

}
