package com.suitebde.database.notifications

import com.suitebde.database.clubs.UsersInClubs
import com.suitebde.database.roles.PermissionsInRoles
import com.suitebde.database.roles.UsersInRoles
import com.suitebde.database.users.Users
import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.roles.Permission
import com.suitebde.repositories.notifications.INotificationTokensRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NotificationTokensDatabaseRepository(
    private val database: IDatabase,
) : INotificationTokensRepository {

    init {
        database.transaction {
            SchemaUtils.create(NotificationTokens)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<NotificationToken> =
        database.suspendedTransaction {
            NotificationTokens
                .selectAll()
                .where { NotificationTokens.userId eq parentId }
                .map(NotificationTokens::toNotificationToken)
        }

    override suspend fun listByPermission(permission: Permission): List<NotificationToken> =
        database.suspendedTransaction {
            NotificationTokens
                .join(Users, JoinType.INNER, NotificationTokens.userId, Users.id)
                .join(UsersInRoles, JoinType.LEFT, NotificationTokens.userId, UsersInRoles.userId)
                .join(PermissionsInRoles, JoinType.LEFT, UsersInRoles.roleId, PermissionsInRoles.roleId)
                .selectAll()
                .where { PermissionsInRoles.permission eq permission.name or Users.superuser }
                .groupBy(NotificationTokens.token)
                .map(NotificationTokens::toNotificationToken)
        }

    override suspend fun listByClub(clubId: UUID): List<NotificationToken> =
        database.suspendedTransaction {
            NotificationTokens
                .join(UsersInClubs, JoinType.INNER, NotificationTokens.userId, UsersInClubs.userId)
                .selectAll()
                .where { UsersInClubs.clubId eq clubId }
                .map(NotificationTokens::toNotificationToken)
        }

    override suspend fun create(
        payload: CreateNotificationTokenPayload,
        parentId: UUID,
        context: IContext?,
    ): NotificationToken? =
        database.suspendedTransaction {
            try {
                NotificationTokens.insert {
                    it[token] = payload.token
                    it[userId] = parentId
                }.resultedValues?.map(NotificationTokens::toNotificationToken)?.singleOrNull()
            } catch (e: ExposedSQLException) {
                NotificationTokens
                    .selectAll()
                    .where { NotificationTokens.token eq payload.token and (NotificationTokens.userId eq parentId) }
                    .map(NotificationTokens::toNotificationToken)
                    .singleOrNull()
            }
        }

    override suspend fun delete(id: String, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            NotificationTokens.deleteWhere {
                token eq id and (userId eq parentId)
            }
        } == 1

    override suspend fun delete(token: String): Boolean =
        database.suspendedTransaction {
            NotificationTokens.deleteWhere { NotificationTokens.token eq token }
        } == 1

}
