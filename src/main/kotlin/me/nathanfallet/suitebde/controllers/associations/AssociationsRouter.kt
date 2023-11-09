package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.ktor.routers.routers.api.APIModelRouter
import me.nathanfallet.ktor.routers.routers.base.ConcatModelRouter
import me.nathanfallet.suitebde.controllers.models.AdminModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class AssociationsRouter(
    associationsController: IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : ConcatModelRouter<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    listOf(
        APIModelRouter(
            Association::class,
            CreateAssociationPayload::class,
            UpdateAssociationPayload::class,
            associationsController,
            prefix = "/api/v1"
        ),
        AdminModelRouter(
            Association::class,
            CreateAssociationPayload::class,
            UpdateAssociationPayload::class,
            associationsController,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    )
)
