package me.nathanfallet.suitebde.controllers.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.*
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class UsersInRolesRouter(
    controller: IUsersInRolesController,
    rolesRouter: RolesRouter,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatChildModelRouter<UserInRole, String, CreateUserInRolePayload, Unit, Role, String>(
    APIChildModelRouter(
        typeInfo<UserInRole>(),
        typeInfo<CreateUserInRolePayload>(),
        typeInfo<Unit>(),
        controller,
        IUsersInRolesController::class,
        rolesRouter,
        route = "users",
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<UserInRole>(),
        typeInfo<CreateUserInRolePayload>(),
        typeInfo<Unit>(),
        controller,
        IUsersInRolesController::class,
        rolesRouter.routerOf<AdminChildModelRouter<Role, String, CreateRolePayload, UpdateRolePayload, Association, String>>(),
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "users",
    ),
)
