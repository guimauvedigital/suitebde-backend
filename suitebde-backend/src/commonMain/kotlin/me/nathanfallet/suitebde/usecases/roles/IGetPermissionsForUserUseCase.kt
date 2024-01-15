package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetPermissionsForUserUseCase : ISuspendUseCase<User, Set<Permission>>
