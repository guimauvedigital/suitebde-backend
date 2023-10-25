package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ICheckPermissionUseCase : ISuspendUseCase<Pair<User, Permission>, Boolean>