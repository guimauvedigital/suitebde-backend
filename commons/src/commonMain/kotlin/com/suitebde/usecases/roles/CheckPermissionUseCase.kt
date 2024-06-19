package com.suitebde.usecases.roles

import com.suitebde.models.roles.AdminPermission
import com.suitebde.models.roles.PermissionInAssociation
import com.suitebde.models.users.User
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.permissions.IPermission
import dev.kaccelero.commons.permissions.IPermittee
import dev.kaccelero.models.UUID

class CheckPermissionUseCase(
    private val getPermissionsForUserUseCase: IGetPermissionsForUserUseCase,
) : ICheckPermissionSuspendUseCase {

    private val adminAssociation = UUID("00000000-0000-0000-0000-000000000000")

    override suspend fun invoke(input1: IPermittee, input2: IPermission): Boolean {
        val user = input1 as? User ?: return false
        return when (input2) {
            is AdminPermission -> input1.associationId == adminAssociation

            is PermissionInAssociation ->
                if (user.associationId == adminAssociation) true
                else if (user.associationId != input2.associationId) false
                else if (user.superuser) true
                else getPermissionsForUserUseCase(user).contains(input2.permission)

            else -> false
        }

    }

}
