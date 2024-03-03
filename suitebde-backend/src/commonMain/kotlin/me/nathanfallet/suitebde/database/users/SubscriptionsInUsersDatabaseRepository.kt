package me.nathanfallet.suitebde.database.users

import me.nathanfallet.suitebde.database.associations.SubscriptionsInAssociations
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
            SchemaUtils.create(SubscriptionsInAssociations)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<SubscriptionInUser> =
        database.suspendedTransaction {
            SubscriptionsInUsers
                .join(
                    SubscriptionsInAssociations,
                    JoinType.INNER,
                    SubscriptionsInUsers.subscriptionId,
                    SubscriptionsInAssociations.id
                )
                .selectAll()
                .where { SubscriptionsInUsers.userId eq parentId }
                .map {
                    SubscriptionsInUsers.toSubscriptionInUser(
                        it,
                        SubscriptionsInAssociations.toSubscriptionInAssociation(it)
                    )
                }
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): SubscriptionInUser? =
        database.suspendedTransaction {
            SubscriptionsInUsers
                .join(
                    SubscriptionsInAssociations,
                    JoinType.INNER,
                    SubscriptionsInUsers.subscriptionId,
                    SubscriptionsInAssociations.id
                )
                .selectAll()
                .where { SubscriptionsInUsers.id eq id and (SubscriptionsInUsers.userId eq parentId) }
                .map {
                    SubscriptionsInUsers.toSubscriptionInUser(
                        it,
                        SubscriptionsInAssociations.toSubscriptionInAssociation(it)
                    )
                }
                .singleOrNull()
        }

    override suspend fun create(
        payload: CreateSubscriptionInUserPayload,
        parentId: String,
        context: IContext?,
    ): SubscriptionInUser? =
        database.suspendedTransaction {
            val id = SubscriptionsInUsers.generateId()
            SubscriptionsInUsers.insert {
                it[SubscriptionsInUsers.id] = id
                it[userId] = parentId
                it[subscriptionId] = payload.subscriptionId
                it[startsAt] = payload.startsAt.toString()
                it[endsAt] = payload.endsAt.toString()
            }
            id
        }.let { id -> get(id, parentId, context) }

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
