package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInRolesRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll

class PermissionsInRolesDatabaseRepository(
    private val database: IDatabase,
) : IPermissionsInRolesRepository {

    init {
        database.transaction {
            SchemaUtils.create(PermissionsInRoles)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<PermissionInRole> {
        return database.suspendedTransaction {
            PermissionsInRoles
                .selectAll()
                .where { PermissionsInRoles.roleId eq parentId }
                .map(PermissionsInRoles::toPermissionInRole)
        }
    }

    override suspend fun listForUser(userId: String): List<PermissionInRole> {
        return database.suspendedTransaction {
            PermissionsInRoles
                .join(UsersInRoles, JoinType.INNER, PermissionsInRoles.roleId, UsersInRoles.roleId)
                .selectAll()
                .where { UsersInRoles.userId eq userId }
                .map(PermissionsInRoles::toPermissionInRole)
        }
    }

}
