package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.repositories.associations.ISubscriptionsInAssociationsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.pagination.Pagination
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class SubscriptionsInAssociationsDatabaseRepository(
    private val database: IDatabase,
) : ISubscriptionsInAssociationsRepository {

    init {
        database.transaction {
            SchemaUtils.create(SubscriptionsInAssociations)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<SubscriptionInAssociation> =
        database.suspendedTransaction {
            SubscriptionsInAssociations
                .selectAll()
                .where { SubscriptionsInAssociations.associationId eq parentId }
                .map(SubscriptionsInAssociations::toSubscriptionInAssociation)
        }

    override suspend fun list(
        pagination: Pagination,
        parentId: String,
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
        parentId: String,
        context: IContext?,
    ): SubscriptionInAssociation? =
        database.suspendedTransaction {
            SubscriptionsInAssociations.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[price] = payload.price
                it[duration] = payload.duration
                it[autoRenewable] = payload.autoRenewable
            }.resultedValues?.map(SubscriptionsInAssociations::toSubscriptionInAssociation)?.singleOrNull()
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): SubscriptionInAssociation? =
        database.suspendedTransaction {
            SubscriptionsInAssociations
                .selectAll()
                .where { SubscriptionsInAssociations.id eq id and (SubscriptionsInAssociations.associationId eq parentId) }
                .map(SubscriptionsInAssociations::toSubscriptionInAssociation)
                .singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateSubscriptionInAssociationPayload,
        parentId: String,
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

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            SubscriptionsInAssociations.deleteWhere {
                SubscriptionsInAssociations.id eq id and (associationId eq parentId)
            } == 1
        }

}
