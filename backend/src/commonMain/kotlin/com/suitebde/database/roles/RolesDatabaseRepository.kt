package com.suitebde.database.roles

import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import com.suitebde.repositories.roles.IRolesRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import org.jetbrains.exposed.sql.*

class RolesDatabaseRepository(
    private val database: IDatabase,
) : IRolesRepository {

    init {
        database.transaction {
            SchemaUtils.create(Roles)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<Role> =
        database.suspendedTransaction {
            Roles
                .selectAll()
                .where { Roles.associationId eq parentId }
                .map(Roles::toRole)
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<Role> =
        database.suspendedTransaction {
            Roles
                .selectAll()
                .where { Roles.associationId eq parentId }
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(Roles::toRole)
        }

    override suspend fun create(payload: CreateRolePayload, parentId: UUID, context: IContext?): Role? =
        database.suspendedTransaction {
            Roles.insert {
                it[associationId] = parentId
                it[name] = payload.name
            }.resultedValues?.map(Roles::toRole)?.singleOrNull()
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): Role? =
        database.suspendedTransaction {
            Roles
                .selectAll()
                .where { Roles.id eq id and (Roles.associationId eq parentId) }
                .map(Roles::toRole)
                .singleOrNull()
        }

    override suspend fun update(id: UUID, payload: UpdateRolePayload, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Roles.update({ Roles.id eq id and (Roles.associationId eq parentId) }) {
                it[name] = payload.name
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Roles.deleteWhere {
                (Roles.id eq id) and (associationId eq parentId)
            }
        } == 1

}
