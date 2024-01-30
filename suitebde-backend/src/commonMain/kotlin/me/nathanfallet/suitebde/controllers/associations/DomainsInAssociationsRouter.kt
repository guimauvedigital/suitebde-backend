package me.nathanfallet.suitebde.controllers.associations

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class DomainsInAssociationsRouter(
    domainsInAssociationsController: IDomainsInAssociationsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>(
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
        getAdminMenuForCallUseCase,
        route = "domains"
    )
)
