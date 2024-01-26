package me.nathanfallet.suitebde.controllers.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.models.roles.CreateUserInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole

class UsersInRolesRouter(
    rolesController: IUsersInRolesController,
    rolesRouter: RolesRouter,
) : APIChildModelRouter<UserInRole, String, CreateUserInRole, Unit, Role, String>(
    typeInfo<UserInRole>(),
    typeInfo<CreateUserInRole>(),
    typeInfo<Unit>(),
    rolesController,
    IUsersInRolesController::class,
    rolesRouter,
    route = "users",
    prefix = "/api/v1"
)
