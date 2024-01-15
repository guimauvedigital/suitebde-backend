package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll

class PermissionsInRolesDatabaseRepository(
    private val database: IDatabase,
) : IChildModelSuspendRepository<PermissionInRole, String, Unit, Unit, String> {

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

}
