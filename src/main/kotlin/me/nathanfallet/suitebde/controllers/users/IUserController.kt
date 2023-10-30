package me.nathanfallet.suitebde.controllers.users

import me.nathanfallet.suitebde.controllers.models.IModelController
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User

interface IUserController : IModelController<User, Unit, UpdateUserPayload>