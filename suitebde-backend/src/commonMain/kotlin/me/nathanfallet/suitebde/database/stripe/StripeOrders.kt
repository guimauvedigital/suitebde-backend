package me.nathanfallet.suitebde.database.stripe

import kotlinx.datetime.toInstant
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object StripeOrders : Table() {

    val sessionId = varchar("session_id", 255)
    val associationId = varchar("association_id", 32).index()
    val email = varchar("email", 255)
    val items = text("items")
    val paidAt = varchar("paid_at", 255).nullable()

    override val primaryKey = PrimaryKey(sessionId)

    fun toStripeOrder(
        row: ResultRow,
    ) = StripeOrder(
        row[sessionId],
        row[associationId],
        row[email],
        SuiteBDEJson.json.decodeFromString(row[items]),
        row[paidAt]?.toInstant()
    )

}
