package com.suitebde.controllers.roles

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationForCallRouter
import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class RolesRouter(
    rolesController: IRolesController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<Role, UUID, CreateRolePayload, UpdateRolePayload, Association, UUID>(
    APIChildModelRouter(
        typeInfo<Role>(),
        typeInfo<CreateRolePayload>(),
        typeInfo<UpdateRolePayload>(),
        rolesController,
        IRolesController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<Role>(),
        typeInfo<CreateRolePayload>(),
        typeInfo<UpdateRolePayload>(),
        rolesController,
        IRolesController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    )
)
