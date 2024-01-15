package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.roles.PermissionInAssociation
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import me.nathanfallet.usecases.permissions.IPermission
import me.nathanfallet.usecases.permissions.IPermittee

class CheckPermissionUseCase(
    private val getPermissionsForUserUseCase: IGetPermissionsForUserUseCase,
) : ICheckPermissionSuspendUseCase {

    private val adminAssociation = "admin"

    override suspend fun invoke(input1: IPermittee, input2: IPermission): Boolean {
        val user = input1 as? User ?: return false
        return when (input2) {
            is AdminPermission -> input1.associationId == adminAssociation

            is PermissionInAssociation ->
                if (user.associationId == adminAssociation) true
                else if (user.associationId != input2.association.id) false
                else if (user.superuser) true
                else getPermissionsForUserUseCase(user).contains(input2.permission)

            else -> false
        }

    }

}
