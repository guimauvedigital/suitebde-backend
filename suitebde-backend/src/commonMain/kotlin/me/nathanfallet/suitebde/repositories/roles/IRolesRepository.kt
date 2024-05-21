package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IRolesRepository : IChildModelSuspendRepository<Role, String, CreateRolePayload, UpdateRolePayload, String>
