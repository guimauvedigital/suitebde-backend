package com.suitebde.usecases.roles

import com.suitebde.models.roles.AdminPermission
import com.suitebde.models.roles.PermissionInAssociation
import com.suitebde.models.users.User
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.permissions.IPermission
import dev.kaccelero.commons.permissions.IPermittee

class CheckPermissionUseCase(
    private val getPermissionsForUserUseCase: IGetPermissionsForUserUseCase,
) : ICheckPermissionSuspendUseCase {

    override suspend fun invoke(input1: IPermittee, input2: IPermission): Boolean {
        val user = input1 as? User ?: return false
        return when (input2) {
            is AdminPermission -> input1.associationId == AdminPermission.adminAssociationId

            is PermissionInAssociation ->
                if (user.associationId == AdminPermission.adminAssociationId) true
                else if (user.associationId != input2.associationId) false
                else if (user.superuser) true
                else getPermissionsForUserUseCase(user).contains(input2.permission)

            else -> false
        }

    }

}
