package me.nathanfallet.suitebde.controllers.dashboard

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class DashboardRouter(
    controller: IDashboardController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : AdminModelRouter<Association, String, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    controller,
    IDashboardController::class,
    getLocaleForCallUseCase,
    translateUseCase,
    requireUserForCallUseCase,
    getAssociationForCallUseCase,
    getAdminMenuForCallUseCase,
    route = ""
)
