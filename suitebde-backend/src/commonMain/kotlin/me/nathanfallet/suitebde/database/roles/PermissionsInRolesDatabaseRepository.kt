package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInRolesRepository
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

    override suspend fun list(roleId: String): List<Permission> {
        return database.suspendedTransaction {
            PermissionsInRoles
                .selectAll()
                .where { PermissionsInRoles.roleId eq roleId }
                .map { Permission.valueOf(it[PermissionsInRoles.permission]) }
        }
    }

}
