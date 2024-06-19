package com.suitebde.database.roles

import com.suitebde.database.users.Users
import com.suitebde.models.roles.CreateUserInRolePayload
import com.suitebde.models.roles.UserInRole
import com.suitebde.repositories.roles.IUsersInRolesRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.*

class UsersInRolesDatabaseRepository(
    private val database: IDatabase,
) : IUsersInRolesRepository {

    init {
        database.transaction {
            SchemaUtils.create(UsersInRoles)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<UserInRole> =
        database.suspendedTransaction {
            UsersInRoles
                .join(Users, JoinType.INNER, UsersInRoles.userId, Users.id)
                .selectAll()
                .where { UsersInRoles.roleId eq parentId }
                .map {
                    UsersInRoles.toUserInRole(it, user = Users.toUser(it))
                }
        }

    override suspend fun listForUser(userId: UUID, associationId: UUID): List<UserInRole> =
        database.suspendedTransaction {
            UsersInRoles
                .join(Roles, JoinType.INNER, UsersInRoles.roleId, Roles.id)
                .selectAll()
                .where { UsersInRoles.userId eq userId and (Roles.associationId eq associationId) }
                .map {
                    UsersInRoles.toUserInRole(it, role = Roles.toRole(it))
                }
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): UserInRole? =
        database.suspendedTransaction {
            UsersInRoles
                .join(Users, JoinType.INNER, UsersInRoles.userId, Users.id)
                .selectAll()
                .where { UsersInRoles.roleId eq parentId and (UsersInRoles.userId eq id) }
                .map {
                    UsersInRoles.toUserInRole(it, user = Users.toUser(it))
                }
                .singleOrNull()
        }

    override suspend fun create(payload: CreateUserInRolePayload, parentId: UUID, context: IContext?): UserInRole? =
        database.suspendedTransaction {
            UsersInRoles.insert {
                it[roleId] = parentId
                it[userId] = payload.userId
            }.resultedValues?.map(UsersInRoles::toUserInRole)?.singleOrNull()
        }

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            UsersInRoles.deleteWhere { roleId eq parentId and (userId eq id) }
        } == 1

}
