package me.nathanfallet.suitebde.usecases.roles

import kotlinx.datetime.*
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInUsersRepository

class GetPermissionsForUserUseCase(
    private val repository: IPermissionsInUsersRepository,
) : IGetPermissionsForUserUseCase {

    private data class PermissionsForUser(
        val permissions: List<Permission>,
        val expiresAt: Instant,
    )

    private val cachedPermissions = mutableMapOf<String, PermissionsForUser>()

    override suspend fun invoke(input: User): List<Permission> {
        return cachedPermissions[input.id]?.takeIf {
            it.expiresAt > Clock.System.now()
        }?.permissions ?: run {
            repository.getPermissionsForUser(input.id).also {
                // Cache permissions for 5 seconds (we only fetch them once)
                cachedPermissions[input.id] = PermissionsForUser(
                    it,
                    Clock.System.now().plus(5, DateTimeUnit.SECOND, TimeZone.currentSystemDefault())
                )
            }
        }
    }

}
