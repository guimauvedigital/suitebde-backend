package com.suitebde.database.stripe

import com.suitebde.models.stripe.CreateStripeOrderPayload
import com.suitebde.models.stripe.StripeOrder
import com.suitebde.models.stripe.UpdateStripeOrderPayload
import com.suitebde.repositories.stripe.IStripeOrdersRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.*

class StripeOrdersDatabaseRepository(
    private val database: IDatabase,
) : IStripeOrdersRepository {

    init {
        database.transaction {
            SchemaUtils.create(StripeOrders)
        }
    }

    override suspend fun get(id: String, parentId: UUID, context: IContext?): StripeOrder? =
        database.suspendedTransaction {
            StripeOrders
                .selectAll()
                .where {
                    StripeOrders.sessionId eq id and (StripeOrders.associationId eq parentId)
                }
                .map(StripeOrders::toStripeOrder)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateStripeOrderPayload, parentId: UUID, context: IContext?): StripeOrder? =
        database.suspendedTransaction {
            StripeOrders.insert {
                it[sessionId] = payload.sessionId
                it[associationId] = parentId
                it[email] = payload.email
                it[items] = payload.items
            }.resultedValues?.map(StripeOrders::toStripeOrder)?.singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateStripeOrderPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            StripeOrders.update({ StripeOrders.sessionId eq id and (StripeOrders.associationId eq parentId) }) {
                it[paidAt] = payload.paidAt
            } == 1
        }

}
