package com.suitebde.controllers.web

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationForCallRouter
import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.controllers.models.PublicChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import com.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*

class WebPagesRouter(
    webPagesController: IWebPagesController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<WebPage, UUID, WebPagePayload, WebPagePayload, Association, UUID>(
    APIChildModelRouter(
        typeInfo<WebPage>(),
        typeInfo<WebPagePayload>(),
        typeInfo<WebPagePayload>(),
        webPagesController,
        IWebPagesController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<WebPage>(),
        typeInfo<WebPagePayload>(),
        typeInfo<WebPagePayload>(),
        webPagesController,
        IWebPagesController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    ),
    PublicChildModelRouter(
        typeInfo<WebPage>(),
        typeInfo<WebPagePayload>(),
        typeInfo<WebPagePayload>(),
        webPagesController,
        IWebPagesController::class,
        associationForCallRouter,
        getUserForCallUseCase,
        getPublicMenuForCallUseCase,
        getLocaleForCallUseCase,
        { template, model ->
            respondTemplate(
                template, model + mapOf(
                    "title" to (model["item"] as? WebPage)?.title
                )
            )
        },
        route = "pages"
    )
)
