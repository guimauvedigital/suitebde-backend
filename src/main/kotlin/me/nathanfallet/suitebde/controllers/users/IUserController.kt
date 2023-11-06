package me.nathanfallet.suitebde.controllers.users

import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User

interface IUserController : IModelController<User, String, CreateUserPayload, UpdateUserPayload>