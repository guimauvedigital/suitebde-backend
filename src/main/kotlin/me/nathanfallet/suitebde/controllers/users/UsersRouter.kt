package me.nathanfallet.suitebde.controllers.users

import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class UsersRouter(
    usersController: IChildModelController<User, String, CreateUserPayload, UpdateUserPayload, Association, String>,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: IAssociationForCallRouter
) : ConcatChildModelRouter<User, String, CreateUserPayload, UpdateUserPayload, Association, String>(
    listOf(
        APIChildModelRouter(
            User::class,
            CreateUserPayload::class,
            UpdateUserPayload::class,
            usersController,
            associationsRouter,
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            User::class,
            CreateUserPayload::class,
            UpdateUserPayload::class,
            usersController,
            associationsRouter,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    ),
    associationsRouter
)
