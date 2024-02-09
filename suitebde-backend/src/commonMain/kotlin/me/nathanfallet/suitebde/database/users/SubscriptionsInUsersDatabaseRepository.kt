package me.nathanfallet.suitebde.database.users

import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload
import me.nathanfallet.suitebde.repositories.users.ISubscriptionsInUsersRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class SubscriptionsInUsersDatabaseRepository(
    private val database: IDatabase,
) : ISubscriptionsInUsersRepository {

    init {
        database.transaction {
            SchemaUtils.create(SubscriptionsInUsers)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<SubscriptionInUser> =
        database.suspendedTransaction {
            SubscriptionsInUsers
                .selectAll()
                .where { SubscriptionsInUsers.userId eq parentId }
                .map(SubscriptionsInUsers::toSubscriptionInUser)
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): SubscriptionInUser? =
        database.suspendedTransaction {
            SubscriptionsInUsers
                .selectAll()
                .where { SubscriptionsInUsers.id eq id and (SubscriptionsInUsers.userId eq parentId) }
                .map(SubscriptionsInUsers::toSubscriptionInUser)
                .firstOrNull()
        }

    override suspend fun create(
        payload: CreateSubscriptionInUserPayload,
        parentId: String,
        context: IContext?,
    ): SubscriptionInUser? =
        database.suspendedTransaction {
            SubscriptionsInUsers.insert {
                it[id] = generateId()
                it[userId] = parentId
                it[subscriptionId] = payload.subscriptionId
                it[startsAt] = payload.startsAt.toString()
                it[endsAt] = payload.endsAt.toString()
            }.resultedValues?.map(SubscriptionsInUsers::toSubscriptionInUser)?.singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateSubscriptionInUserPayload,
        parentId: String,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            SubscriptionsInUsers.update({ SubscriptionsInUsers.id eq id and (SubscriptionsInUsers.userId eq parentId) }) {
                it[endsAt] = payload.endsAt.toString()
            }
        } == 1

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            SubscriptionsInUsers.deleteWhere { SubscriptionsInUsers.id eq id and (userId eq parentId) }
        } == 1

}
