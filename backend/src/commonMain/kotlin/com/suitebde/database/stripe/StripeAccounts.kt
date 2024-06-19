package com.suitebde.database.stripe

import com.suitebde.models.stripe.StripeAccount
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object StripeAccounts : Table() {

    val associationId = uuid("association_id").index()
    val accountId = varchar("account_id", 255).index()
    val chargesEnabled = bool("charges_enabled")

    override val primaryKey = PrimaryKey(associationId, accountId)

    fun toStripeAccount(
        row: ResultRow,
    ) = StripeAccount(
        UUID(row[associationId]),
        row[accountId],
        row[chargesEnabled]
    )

}
