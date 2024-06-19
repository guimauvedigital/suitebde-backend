package com.suitebde.controllers.associations

import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class DomainsInAssociationsRouter(
    domainsInAssociationsController: IDomainsInAssociationsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, UUID>(
    APIChildModelRouter(
        typeInfo<DomainInAssociation>(),
        typeInfo<CreateDomainInAssociationPayload>(),
        typeInfo<Unit>(),
        domainsInAssociationsController,
        IDomainsInAssociationsController::class,
        associationsRouter.routerOf(),
        route = "domains",
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<DomainInAssociation>(),
        typeInfo<CreateDomainInAssociationPayload>(),
        typeInfo<Unit>(),
        domainsInAssociationsController,
        IDomainsInAssociationsController::class,
        associationsRouter.routerOf(),
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "domains"
    )
)
