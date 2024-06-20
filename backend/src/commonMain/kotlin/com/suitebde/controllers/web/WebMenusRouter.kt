package com.suitebde.controllers.web

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationForCallRouter
import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class WebMenusRouter(
    webMenusController: IWebMenusController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<WebMenu, UUID, CreateWebMenuPayload, UpdateWebMenuPayload, Association, UUID>(
    APIChildModelRouter(
        typeInfo<WebMenu>(),
        typeInfo<CreateWebMenuPayload>(),
        typeInfo<UpdateWebMenuPayload>(),
        webMenusController,
        IWebMenusController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<WebMenu>(),
        typeInfo<CreateWebMenuPayload>(),
        typeInfo<UpdateWebMenuPayload>(),
        webMenusController,
        IWebMenusController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    )
)
