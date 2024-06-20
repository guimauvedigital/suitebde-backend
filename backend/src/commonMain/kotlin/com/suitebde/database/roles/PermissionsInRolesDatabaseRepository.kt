package com.suitebde.database.roles

import com.suitebde.models.roles.CreatePermissionInRolePayload
import com.suitebde.models.roles.PermissionInRole
import com.suitebde.repositories.roles.IPermissionsInRolesRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PermissionsInRolesDatabaseRepository(
    private val database: IDatabase,
) : IPermissionsInRolesRepository {

    init {
        database.transaction {
            SchemaUtils.create(PermissionsInRoles)
            SchemaUtils.create(UsersInRoles)
            SchemaUtils.create(Roles)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<PermissionInRole> =
        database.suspendedTransaction {
            PermissionsInRoles
                .selectAll()
                .where { PermissionsInRoles.roleId eq parentId }
                .map(PermissionsInRoles::toPermissionInRole)
        }

    override suspend fun listForUser(userId: UUID, associationId: UUID): List<PermissionInRole> =
        database.suspendedTransaction {
            PermissionsInRoles
                .join(UsersInRoles, JoinType.INNER, PermissionsInRoles.roleId, UsersInRoles.roleId)
                .join(Roles, JoinType.INNER, PermissionsInRoles.roleId, Roles.id)
                .selectAll()
                .where { UsersInRoles.userId eq userId and (Roles.associationId eq associationId) }
                .map(PermissionsInRoles::toPermissionInRole)
        }

    override suspend fun create(
        payload: CreatePermissionInRolePayload,
        parentId: UUID,
        context: IContext?,
    ): PermissionInRole? =
        database.suspendedTransaction {
            PermissionsInRoles.insert {
                it[roleId] = parentId
                it[permission] = payload.permission.name
            }.resultedValues?.map(PermissionsInRoles::toPermissionInRole)?.singleOrNull()
        }

    override suspend fun delete(id: String, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            PermissionsInRoles.deleteWhere { roleId eq parentId and (permission eq id) } == 1
        }

}
