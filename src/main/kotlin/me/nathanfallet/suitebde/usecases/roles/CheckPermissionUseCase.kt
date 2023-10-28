package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User

class CheckPermissionUseCase : ICheckPermissionUseCase {

    override suspend fun invoke(input: Triple<User, Association, Permission>): Boolean {
        if (input.first.associationId != input.second.id) return false
        return input.first.superuser
    }

}