package me.nathanfallet.suitebde.database.stripe

import kotlinx.serialization.encodeToString
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.stripe.CreateStripeOrderPayload
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.suitebde.models.stripe.UpdateStripeOrderPayload
import me.nathanfallet.suitebde.repositories.stripe.IStripeOrdersRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*

class StripeOrdersDatabaseRepository(
    private val database: IDatabase,
) : IStripeOrdersRepository {

    init {
        database.transaction {
            SchemaUtils.create(StripeOrders)
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): StripeOrder? =
        database.suspendedTransaction {
            StripeOrders
                .selectAll()
                .where {
                    StripeOrders.sessionId eq id and (StripeOrders.associationId eq parentId)
                }
                .map(StripeOrders::toStripeOrder)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateStripeOrderPayload, parentId: String, context: IContext?): StripeOrder? =
        database.suspendedTransaction {
            StripeOrders.insert {
                it[sessionId] = payload.sessionId
                it[associationId] = parentId
                it[email] = payload.email
                it[items] = SuiteBDEJson.json.encodeToString(payload.items)
            }.resultedValues?.map(StripeOrders::toStripeOrder)?.singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateStripeOrderPayload,
        parentId: String,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            StripeOrders.update({ StripeOrders.sessionId eq id and (StripeOrders.associationId eq parentId) }) {
                it[paidAt] = payload.paidAt?.toString()
            } == 1
        }

}
