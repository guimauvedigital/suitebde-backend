package com.suitebde.repositories.roles

import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IRolesRepository : IChildModelSuspendRepository<Role, UUID, CreateRolePayload, UpdateRolePayload, UUID>
