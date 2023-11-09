package me.nathanfallet.suitebde.controllers.users

import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.ktor.routers.routers.api.APIModelRouter
import me.nathanfallet.ktor.routers.routers.base.ConcatModelRouter
import me.nathanfallet.suitebde.controllers.models.AdminModelRouter
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class UsersRouter(
    usersController: IModelController<User, String, CreateUserPayload, UpdateUserPayload>,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : ConcatModelRouter<User, String, CreateUserPayload, UpdateUserPayload>(
    listOf(
        APIModelRouter(
            User::class,
            CreateUserPayload::class,
            UpdateUserPayload::class,
            usersController,
            "/api/v1"
        ),
        AdminModelRouter(
            User::class,
            CreateUserPayload::class,
            UpdateUserPayload::class,
            usersController,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    )
)
