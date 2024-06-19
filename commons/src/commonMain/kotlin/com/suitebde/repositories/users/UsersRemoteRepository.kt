package com.suitebde.repositories.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.application.SearchOptions
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.IPaginationOptions
import dev.kaccelero.repositories.Pagination
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*

class UsersRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<User, UUID, CreateUserPayload, UpdateUserPayload, UUID>(
    typeInfo<User>(),
    typeInfo<CreateUserPayload>(),
    typeInfo<UpdateUserPayload>(),
    typeInfo<List<User>>(),
    client,
    parentRepository,
    prefix = "/api/v1"
), IUsersRemoteRepository {

    override fun encodePaginationOptions(options: IPaginationOptions, builder: HttpRequestBuilder) = when (options) {
        is SearchOptions -> builder.parameter("search", options.search)
        else -> super.encodePaginationOptions(options, builder)
    }

    override suspend fun list(pagination: Pagination, associationId: UUID): List<User> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): User? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(id: UUID, payload: UpdateUserPayload, associationId: UUID): User? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun listPermissions(id: UUID, associationId: UUID): List<Permission> =
        client
            .request(
                HttpMethod.Get,
                "${constructFullRoute(RecursiveId<Association, UUID, Unit>(associationId))}/$id/permissions"
            )
            .body()

}
