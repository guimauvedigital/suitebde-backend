package com.suitebde.repositories.roles

import com.suitebde.models.roles.CreateUserInRolePayload
import com.suitebde.models.roles.UserInRole
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IUsersInRolesRepository :
    IChildModelSuspendRepository<UserInRole, UUID, CreateUserInRolePayload, Unit, UUID> {

    suspend fun listForUser(userId: UUID, associationId: UUID): List<UserInRole>

}
