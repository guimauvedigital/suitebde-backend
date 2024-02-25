package me.nathanfallet.suitebde.database.stripe

import me.nathanfallet.suitebde.models.stripe.StripeAccount
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object StripeAccounts : Table() {

    val associationId = varchar("association_id", 32).index()
    val accountId = varchar("account_id", 255).index()
    val chargesEnabled = bool("charges_enabled")

    override val primaryKey = PrimaryKey(associationId, accountId)

    fun toStripeAccount(
        row: ResultRow,
    ) = StripeAccount(
        row[associationId],
        row[accountId],
        row[chargesEnabled]
    )

}
