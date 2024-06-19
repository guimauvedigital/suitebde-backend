package com.suitebde.database.associations

import com.suitebde.models.associations.SubscriptionInAssociation
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object SubscriptionsInAssociations : UUIDTable() {

    val associationId = uuid("association_id").index()
    val name = varchar("name", 255)
    val description = text("description")
    val price = double("price")
    val duration = varchar("duration", 255)
    val autoRenewable = bool("auto_renewable")

    fun toSubscriptionInAssociation(
        row: ResultRow,
    ) = SubscriptionInAssociation(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[name],
        row[description],
        row[price],
        row[duration],
        row[autoRenewable]
    )

}
