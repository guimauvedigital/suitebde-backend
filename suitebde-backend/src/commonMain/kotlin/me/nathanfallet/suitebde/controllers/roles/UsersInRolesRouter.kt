package me.nathanfallet.suitebde.controllers.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.models.roles.CreateUserInRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole

class UsersInRolesRouter(
    controller: IUsersInRolesController,
    rolesRouter: RolesRouter,
) : APIChildModelRouter<UserInRole, String, CreateUserInRolePayload, Unit, Role, String>(
    typeInfo<UserInRole>(),
    typeInfo<CreateUserInRolePayload>(),
    typeInfo<Unit>(),
    controller,
    IUsersInRolesController::class,
    rolesRouter,
    route = "users",
    prefix = "/api/v1"
)
