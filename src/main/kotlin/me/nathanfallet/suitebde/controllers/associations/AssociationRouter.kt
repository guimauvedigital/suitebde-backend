package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.suitebde.controllers.models.ModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class AssociationRouter(
    associationController: IAssociationController,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : ModelRouter<Association, Unit, Unit>(
    "associations",
    Association::class,
    Unit::class,
    Unit::class,
    associationController,
    translateUseCase,
    getAdminMenuForCallUseCase
)