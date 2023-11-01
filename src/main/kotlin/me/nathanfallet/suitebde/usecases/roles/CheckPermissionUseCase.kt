package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User

class CheckPermissionUseCase : ICheckPermissionUseCase {

    override suspend fun invoke(input1: User, input2: Association, input3: Permission): Boolean {
        if (input1.associationId != input2.id) return false
        return input1.superuser
    }

}