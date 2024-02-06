package me.nathanfallet.suitebde.controllers.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.models.roles.CreatePermissionInRole
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.models.roles.Role

class PermissionsInRolesRouter(
    controller: IPermissionsInRolesController,
    rolesRouter: RolesRouter,
) : APIChildModelRouter<PermissionInRole, String, CreatePermissionInRole, Unit, Role, String>(
    typeInfo<PermissionInRole>(),
    typeInfo<CreatePermissionInRole>(),
    typeInfo<Unit>(),
    controller,
    IPermissionsInRolesController::class,
    rolesRouter,
    route = "permissions",
    prefix = "/api/v1"
)
