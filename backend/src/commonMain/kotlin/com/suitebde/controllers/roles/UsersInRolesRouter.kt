package com.suitebde.controllers.roles

import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.*
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class UsersInRolesRouter(
    controller: IUsersInRolesController,
    rolesRouter: RolesRouter,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatChildModelRouter<UserInRole, UUID, CreateUserInRolePayload, Unit, Role, UUID>(
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
        rolesRouter.routerOf<AdminChildModelRouter<Role, UUID, CreateRolePayload, UpdateRolePayload, Association, UUID>>(),
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "users",
    ),
)
