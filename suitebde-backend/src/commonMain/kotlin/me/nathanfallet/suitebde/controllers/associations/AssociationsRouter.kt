package me.nathanfallet.suitebde.controllers.associations

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class AssociationsRouter(
    associationsController: IAssociationsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatModelRouter<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    APIModelRouter(
        typeInfo<Association>(),
        typeInfo<CreateAssociationPayload>(),
        typeInfo<UpdateAssociationPayload>(),
        associationsController,
        IAssociationsController::class,
        prefix = "/api/v1"
    ),
    AdminModelRouter(
        typeInfo<Association>(),
        typeInfo<CreateAssociationPayload>(),
        typeInfo<UpdateAssociationPayload>(),
        associationsController,
        IAssociationsController::class,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    )
)
