package com.suitebde.controllers.users

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationForCallRouter
import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class UsersRouter(
    usersController: IUsersController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<User, UUID, CreateUserPayload, UpdateUserPayload, Association, UUID>(
    APIChildModelRouter(
        typeInfo<User>(),
        typeInfo<CreateUserPayload>(),
        typeInfo<UpdateUserPayload>(),
        usersController,
        IUsersController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<User>(),
        typeInfo<CreateUserPayload>(),
        typeInfo<UpdateUserPayload>(),
        usersController,
        IUsersController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    )
)
