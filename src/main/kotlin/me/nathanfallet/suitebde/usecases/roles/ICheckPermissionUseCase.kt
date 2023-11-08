package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.roles.IPermission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface ICheckPermissionUseCase : IPairSuspendUseCase<User, IPermission, Boolean>