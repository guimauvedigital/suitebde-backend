package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User

class CheckPermissionUseCase : ICheckPermissionUseCase {

    override suspend fun invoke(input: Pair<User, Permission>): Boolean {
        return input.first.superuser
    }

}