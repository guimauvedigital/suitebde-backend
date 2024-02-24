package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.models.associations.StripeAccountInAssociation
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object StripeAccountsInAssociations : Table() {

    val associationId = varchar("association_id", 32).index()
    val accountId = varchar("account_id", 255).index()
    val chargesEnabled = bool("charges_enabled")

    override val primaryKey = PrimaryKey(associationId, accountId)

    fun toStripeAccountInAssociation(
        row: ResultRow,
    ) = StripeAccountInAssociation(
        row[associationId],
        row[accountId],
        row[chargesEnabled]
    )

}
