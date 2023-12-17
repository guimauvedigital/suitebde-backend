package me.nathanfallet.suitebde.controllers.associations

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.api.APIMapping
import me.nathanfallet.ktorx.routers.api.APIModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class AssociationsRouter(
    associationsController: IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatModelRouter<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    listOf(
        APIModelRouter(
            typeInfo<Association>(),
            typeInfo<CreateAssociationPayload>(),
            typeInfo<UpdateAssociationPayload>(),
            typeInfo<List<Association>>(),
            associationsController,
            mapping = APIMapping(
                createEnabled = false,
                deleteEnabled = false
            ),
            prefix = "/api/v1"
        ),
        AdminModelRouter(
            typeInfo<Association>(),
            typeInfo<CreateAssociationPayload>(),
            typeInfo<UpdateAssociationPayload>(),
            typeInfo<List<Association>>(),
            associationsController,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    )
)
