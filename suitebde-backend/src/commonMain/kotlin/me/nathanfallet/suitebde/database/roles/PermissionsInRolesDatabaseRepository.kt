package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.suitebde.models.roles.CreatePermissionInRole
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.repositories.roles.IPermissionsInRolesRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PermissionsInRolesDatabaseRepository(
    private val database: IDatabase,
) : IPermissionsInRolesRepository {

    init {
        database.transaction {
            SchemaUtils.create(PermissionsInRoles)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<PermissionInRole> =
        database.suspendedTransaction {
            PermissionsInRoles
                .selectAll()
                .where { PermissionsInRoles.roleId eq parentId }
                .map(PermissionsInRoles::toPermissionInRole)
        }

    override suspend fun listForUser(userId: String): List<PermissionInRole> =
        database.suspendedTransaction {
            PermissionsInRoles
                .join(UsersInRoles, JoinType.INNER, PermissionsInRoles.roleId, UsersInRoles.roleId)
                .selectAll()
                .where { UsersInRoles.userId eq userId }
                .map(PermissionsInRoles::toPermissionInRole)
        }

    override suspend fun create(
        payload: CreatePermissionInRole,
        parentId: String,
        context: IContext?,
    ): PermissionInRole? =
        database.suspendedTransaction {
            PermissionsInRoles.insert {
                it[roleId] = parentId
                it[permission] = payload.permission.name
            }.resultedValues?.map(PermissionsInRoles::toPermissionInRole)?.singleOrNull()
        }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            PermissionsInRoles.deleteWhere { roleId eq parentId and (permission eq id) } == 1
        }

}
