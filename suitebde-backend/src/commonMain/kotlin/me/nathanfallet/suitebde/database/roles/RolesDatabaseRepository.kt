package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RolesDatabaseRepository(
    private val database: IDatabase,
) : IChildModelSuspendRepository<Role, String, CreateRolePayload, UpdateRolePayload, String> {

    init {
        database.transaction {
            SchemaUtils.create(Roles)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<Role> {
        return database.suspendedTransaction {
            Roles
                .selectAll()
                .where { Roles.associationId eq parentId }
                .map(Roles::toRole)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<Role> {
        return database.suspendedTransaction {
            Roles
                .selectAll()
                .where { Roles.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(Roles::toRole)
        }
    }

    override suspend fun create(payload: CreateRolePayload, parentId: String, context: IContext?): Role? {
        return database.suspendedTransaction {
            Roles.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[name] = payload.name
            }.resultedValues?.map(Roles::toRole)?.singleOrNull()
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): Role? {
        return database.suspendedTransaction {
            Roles
                .selectAll()
                .where { Roles.id eq id and (Roles.associationId eq parentId) }
                .map(Roles::toRole)
                .singleOrNull()
        }
    }

    override suspend fun update(id: String, payload: UpdateRolePayload, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            Roles.update({ Roles.id eq id and (Roles.associationId eq parentId) }) {
                it[name] = payload.name
            }
        } == 1
    }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            Roles.deleteWhere {
                (Roles.id eq id) and (associationId eq parentId)
            }
        } == 1
    }

}
