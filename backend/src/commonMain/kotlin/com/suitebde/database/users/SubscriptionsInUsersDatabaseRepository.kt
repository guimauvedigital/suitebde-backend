package com.suitebde.database.users

import com.suitebde.database.associations.SubscriptionsInAssociations
import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import com.suitebde.repositories.users.ISubscriptionsInUsersRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.*

class SubscriptionsInUsersDatabaseRepository(
    private val database: IDatabase,
) : ISubscriptionsInUsersRepository {

    init {
        database.transaction {
            SchemaUtils.create(SubscriptionsInUsers)
            SchemaUtils.create(SubscriptionsInAssociations)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<SubscriptionInUser> =
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

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): SubscriptionInUser? =
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
        parentId: UUID,
        context: IContext?,
    ): SubscriptionInUser? =
        database.suspendedTransaction {
            SubscriptionsInUsers.insertAndGetId {
                it[userId] = parentId
                it[subscriptionId] = payload.subscriptionId
                it[startsAt] = payload.startsAt
                it[endsAt] = payload.endsAt
            }
        }.let { id -> get(UUID(id.value), parentId, context) }

    override suspend fun update(
        id: UUID,
        payload: UpdateSubscriptionInUserPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            SubscriptionsInUsers.update({ SubscriptionsInUsers.id eq id and (SubscriptionsInUsers.userId eq parentId) }) {
                it[endsAt] = payload.endsAt
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            SubscriptionsInUsers.deleteWhere { SubscriptionsInUsers.id eq id and (userId eq parentId) }
        } == 1

}
