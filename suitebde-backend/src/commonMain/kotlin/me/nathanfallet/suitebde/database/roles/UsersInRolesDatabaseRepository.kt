package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.suitebde.database.users.Users
import me.nathanfallet.suitebde.models.roles.CreateUserInRolePayload
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.suitebde.repositories.roles.IUsersInRolesRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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

    override suspend fun list(parentId: String, context: IContext?): List<UserInRole> =
        database.suspendedTransaction {
            UsersInRoles
                .join(Users, JoinType.INNER, UsersInRoles.userId, Users.id)
                .selectAll()
                .where { UsersInRoles.roleId eq parentId }
                .map {
                    UsersInRoles.toUserInRole(it, user = Users.toUser(it))
                }
        }

    override suspend fun listForUser(userId: String): List<UserInRole> =
        database.suspendedTransaction {
            UsersInRoles
                .join(Roles, JoinType.INNER, UsersInRoles.roleId, Roles.id)
                .selectAll()
                .where { UsersInRoles.userId eq userId }
                .map {
                    UsersInRoles.toUserInRole(it, role = Roles.toRole(it))
                }
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): UserInRole? =
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

    override suspend fun create(payload: CreateUserInRolePayload, parentId: String, context: IContext?): UserInRole? =
        database.suspendedTransaction {
            UsersInRoles.insert {
                it[roleId] = parentId
                it[userId] = payload.userId
            }.resultedValues?.map(UsersInRoles::toUserInRole)?.singleOrNull()
        }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            UsersInRoles.deleteWhere { roleId eq parentId and (userId eq id) }
        } == 1

}
