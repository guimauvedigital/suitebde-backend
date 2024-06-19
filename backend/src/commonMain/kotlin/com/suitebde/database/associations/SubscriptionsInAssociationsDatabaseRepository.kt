package com.suitebde.database.associations

import com.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import com.suitebde.repositories.associations.ISubscriptionsInAssociationsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import org.jetbrains.exposed.sql.*

class SubscriptionsInAssociationsDatabaseRepository(
    private val database: IDatabase,
) : ISubscriptionsInAssociationsRepository {

    init {
        database.transaction {
            SchemaUtils.create(SubscriptionsInAssociations)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<SubscriptionInAssociation> =
        database.suspendedTransaction {
            SubscriptionsInAssociations
                .selectAll()
                .where { SubscriptionsInAssociations.associationId eq parentId }
                .map(SubscriptionsInAssociations::toSubscriptionInAssociation)
        }

    override suspend fun list(
        pagination: Pagination,
        parentId: UUID,
        context: IContext?,
    ): List<SubscriptionInAssociation> =
        database.suspendedTransaction {
            SubscriptionsInAssociations
                .selectAll()
                .where { SubscriptionsInAssociations.associationId eq parentId }
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(SubscriptionsInAssociations::toSubscriptionInAssociation)
        }

    override suspend fun create(
        payload: CreateSubscriptionInAssociationPayload,
        parentId: UUID,
        context: IContext?,
    ): SubscriptionInAssociation? =
        database.suspendedTransaction {
            SubscriptionsInAssociations.insert {
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[price] = payload.price
                it[duration] = payload.duration
                it[autoRenewable] = payload.autoRenewable
            }.resultedValues?.map(SubscriptionsInAssociations::toSubscriptionInAssociation)?.singleOrNull()
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): SubscriptionInAssociation? =
        database.suspendedTransaction {
            SubscriptionsInAssociations
                .selectAll()
                .where { SubscriptionsInAssociations.id eq id and (SubscriptionsInAssociations.associationId eq parentId) }
                .map(SubscriptionsInAssociations::toSubscriptionInAssociation)
                .singleOrNull()
        }

    override suspend fun update(
        id: UUID,
        payload: UpdateSubscriptionInAssociationPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            SubscriptionsInAssociations.update({ SubscriptionsInAssociations.id eq id and (SubscriptionsInAssociations.associationId eq parentId) }) {
                payload.name?.let { name -> it[SubscriptionsInAssociations.name] = name }
                payload.description?.let { description -> it[SubscriptionsInAssociations.description] = description }
                payload.price?.let { price -> it[SubscriptionsInAssociations.price] = price }
                payload.duration?.let { duration -> it[SubscriptionsInAssociations.duration] = duration }
                payload.autoRenewable?.let { autoRenewable ->
                    it[SubscriptionsInAssociations.autoRenewable] = autoRenewable
                }
            } == 1
        }

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            SubscriptionsInAssociations.deleteWhere {
                SubscriptionsInAssociations.id eq id and (associationId eq parentId)
            } == 1
        }

}
