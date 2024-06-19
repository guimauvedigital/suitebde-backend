package com.suitebde.controllers.associations

import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class SubscriptionsInAssociationsRouter(
    controller: ISubscriptionsInAssociationsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<SubscriptionInAssociation, UUID, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, Association, UUID>(
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
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "subscriptions"
    )
)
