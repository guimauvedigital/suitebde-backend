package me.nathanfallet.suitebde.controllers.roles

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class RolesRouter(
    rolesController: IChildModelController<Role, String, CreateRolePayload, UpdateRolePayload, Association, String>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<Role, String, CreateRolePayload, UpdateRolePayload, Association, String>(
    listOf(
        APIChildModelRouter(
            typeInfo<Role>(),
            typeInfo<CreateRolePayload>(),
            typeInfo<UpdateRolePayload>(),
            typeInfo<List<Role>>(),
            rolesController,
            associationsRouter,
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            typeInfo<Role>(),
            typeInfo<CreateRolePayload>(),
            typeInfo<UpdateRolePayload>(),
            typeInfo<List<Role>>(),
            rolesController,
            associationForCallRouter,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    ),
    associationsRouter
)
