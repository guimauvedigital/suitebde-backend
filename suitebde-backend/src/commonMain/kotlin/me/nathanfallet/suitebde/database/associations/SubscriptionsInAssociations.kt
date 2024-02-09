package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object SubscriptionsInAssociations : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val name = varchar("name", 255)
    val description = text("description")
    val price = double("price")
    val duration = varchar("duration", 255)
    val autoRenewable = bool("auto_renewable")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toSubscriptionInAssociation(
        row: ResultRow,
    ) = SubscriptionInAssociation(
        row[id],
        row[associationId],
        row[name],
        row[description],
        row[price],
        row[duration],
        row[autoRenewable]
    )

}
