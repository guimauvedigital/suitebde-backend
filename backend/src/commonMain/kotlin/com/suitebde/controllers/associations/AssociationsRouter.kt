package com.suitebde.controllers.associations

import com.suitebde.controllers.models.AdminModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIModelRouter
import dev.kaccelero.routers.ConcatModelRouter
import io.ktor.util.reflect.*

class AssociationsRouter(
    associationsController: IAssociationsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatModelRouter<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload>(
    APIModelRouter(
        typeInfo<Association>(),
        typeInfo<CreateAssociationPayload>(),
        typeInfo<UpdateAssociationPayload>(),
        associationsController,
        IAssociationsController::class,
        prefix = "/api/v1"
    ),
    AdminModelRouter(
        typeInfo<Association>(),
        typeInfo<CreateAssociationPayload>(),
        typeInfo<UpdateAssociationPayload>(),
        associationsController,
        IAssociationsController::class,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    )
)
