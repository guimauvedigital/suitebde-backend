package com.suitebde.usecases.roles

import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.repositories.roles.IPermissionsInRolesRepository
import dev.kaccelero.models.UUID
import kotlinx.datetime.*

class GetPermissionsForUserUseCase(
    private val repository: IPermissionsInRolesRepository,
) : IGetPermissionsForUserUseCase {

    private data class PermissionsForUser(
        val permissions: Set<Permission>,
        val expiresAt: Instant,
    )

    private val cachedPermissions = mutableMapOf<UUID, PermissionsForUser>()

    override suspend fun invoke(input: User): Set<Permission> {
        return cachedPermissions[input.id]?.takeIf {
            it.expiresAt > Clock.System.now()
        }?.permissions ?: run {
            repository.listForUser(input.id, input.associationId).map { it.permission }.toSet().also {
                // Cache permissions for 5 seconds (we only fetch them once)
                cachedPermissions[input.id] = PermissionsForUser(
                    it,
                    Clock.System.now().plus(5, DateTimeUnit.SECOND, TimeZone.currentSystemDefault())
                )
            }
        }
    }

}
