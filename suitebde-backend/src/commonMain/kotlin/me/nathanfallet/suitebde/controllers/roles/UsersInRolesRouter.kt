package me.nathanfallet.suitebde.controllers.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.api.APIMapping
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.models.roles.CreateUserInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole

class UsersInRolesRouter(
    rolesController: IChildModelController<UserInRole, String, CreateUserInRole, Unit, Role, String>,
    rolesRouter: RolesRouter,
) : APIChildModelRouter<UserInRole, String, CreateUserInRole, Unit, Role, String>(
    typeInfo<UserInRole>(),
    typeInfo<CreateUserInRole>(),
    typeInfo<Unit>(),
    typeInfo<List<UserInRole>>(),
    rolesController,
    rolesRouter,
    mapping = APIMapping(
        updateEnabled = false
    ),
    route = "users",
    prefix = "/api/v1"
)
