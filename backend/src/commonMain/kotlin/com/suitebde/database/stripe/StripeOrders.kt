package com.suitebde.database.stripe

import com.suitebde.models.stripe.CheckoutItem
import com.suitebde.models.stripe.StripeOrder
import dev.kaccelero.models.UUID
import dev.kaccelero.serializers.Serialization
import kotlinx.serialization.serializer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object StripeOrders : Table() {

    val sessionId = varchar("session_id", 255)
    val associationId = uuid("association_id").index()
    val email = varchar("email", 255)
    val items = json("items", Serialization.json, serializer<List<CheckoutItem>>())
    val paidAt = timestamp("paid_at").nullable()

    override val primaryKey = PrimaryKey(sessionId)

    fun toStripeOrder(
        row: ResultRow,
    ) = StripeOrder(
        row[sessionId],
        UUID(row[associationId]),
        row[email],
        row[items],
        row[paidAt]
    )

}
