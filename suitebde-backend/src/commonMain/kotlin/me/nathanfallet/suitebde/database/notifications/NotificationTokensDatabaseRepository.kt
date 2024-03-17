package me.nathanfallet.suitebde.database.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.repositories.notifications.INotificationTokensRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
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

    override suspend fun list(parentId: String, context: IContext?): List<NotificationToken> =
        database.suspendedTransaction {
            NotificationTokens
                .selectAll()
                .where { NotificationTokens.userId eq parentId }
                .map(NotificationTokens::toNotificationToken)
        }

    override suspend fun create(
        payload: CreateNotificationTokenPayload,
        parentId: String,
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

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            NotificationTokens.deleteWhere {
                token eq id and (userId eq parentId)
            }
        } == 1

}
