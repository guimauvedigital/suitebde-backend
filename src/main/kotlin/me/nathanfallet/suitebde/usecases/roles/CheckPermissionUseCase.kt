package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.roles.IPermission
import me.nathanfallet.suitebde.models.roles.PermissionInAssociation
import me.nathanfallet.suitebde.models.users.User

class CheckPermissionUseCase : ICheckPermissionUseCase {

    private val adminAssociation = "admin"

    override suspend fun invoke(input1: User, input2: IPermission): Boolean {
        return when (input2) {
            is AdminPermission -> {
                input1.associationId == adminAssociation
            }

            is PermissionInAssociation -> {
                if (input1.associationId == adminAssociation) return true
                if (input1.associationId != input2.association.id) return false
                input1.superuser
            }

            else -> false
        }

    }

}