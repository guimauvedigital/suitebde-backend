package com.suitebde.repositories.roles

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.util.reflect.*

class RolesRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<Role, UUID, CreateRolePayload, UpdateRolePayload, UUID>(
    typeInfo<Role>(),
    typeInfo<CreateRolePayload>(),
    typeInfo<UpdateRolePayload>(),
    typeInfo<List<Role>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IRolesRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: UUID): List<Role> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): Role? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(payload: CreateRolePayload, associationId: UUID): Role? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(id: UUID, payload: UpdateRolePayload, associationId: UUID): Role? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

}
