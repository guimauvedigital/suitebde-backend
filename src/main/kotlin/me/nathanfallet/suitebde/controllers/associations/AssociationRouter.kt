package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.suitebde.controllers.models.ModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class AssociationRouter(
    associationController: IAssociationController,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : ModelRouter<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    Association::class,
    CreateAssociationPayload::class,
    UpdateAssociationPayload::class,
    associationController,
    translateUseCase,
    getAdminMenuForCallUseCase
)