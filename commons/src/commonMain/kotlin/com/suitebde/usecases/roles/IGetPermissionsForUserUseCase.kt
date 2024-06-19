package com.suitebde.usecases.roles

import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetPermissionsForUserUseCase : ISuspendUseCase<User, Set<Permission>>
