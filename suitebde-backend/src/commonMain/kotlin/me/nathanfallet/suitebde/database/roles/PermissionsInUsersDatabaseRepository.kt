package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInUsersRepository
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.selectAll

class PermissionsInUsersDatabaseRepository(
    private val database: IDatabase,
) : IPermissionsInUsersRepository {

    override suspend fun getPermissionsForUser(userId: String): List<Permission> {
        return database.suspendedTransaction {
            PermissionsInRoles
                .join(UsersInRoles, JoinType.INNER, PermissionsInRoles.roleId, UsersInRoles.roleId)
                .selectAll()
                .where { UsersInRoles.userId eq userId }
                .map { Permission.valueOf(it[PermissionsInRoles.permission]) }
        }
    }

}
