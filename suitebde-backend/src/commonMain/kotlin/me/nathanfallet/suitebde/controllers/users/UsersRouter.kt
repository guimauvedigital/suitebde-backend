package me.nathanfallet.suitebde.controllers.users

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class UsersRouter(
    usersController: IUsersController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<User, String, CreateUserPayload, UpdateUserPayload, Association, String>(
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
