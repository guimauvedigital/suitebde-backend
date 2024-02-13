package me.nathanfallet.suitebde.controllers.associations

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class SubscriptionsInAssociationsRouter(
    controller: ISubscriptionsInAssociationsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<SubscriptionInAssociation, String, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, Association, String>(
    APIChildModelRouter(
        typeInfo<SubscriptionInAssociation>(),
        typeInfo<CreateSubscriptionInAssociationPayload>(),
        typeInfo<UpdateSubscriptionInAssociationPayload>(),
        controller,
        ISubscriptionsInAssociationsController::class,
        associationsRouter,
        route = "subscriptions",
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<SubscriptionInAssociation>(),
        typeInfo<CreateSubscriptionInAssociationPayload>(),
        typeInfo<UpdateSubscriptionInAssociationPayload>(),
        controller,
        ISubscriptionsInAssociationsController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "subscriptions"
    )
)
