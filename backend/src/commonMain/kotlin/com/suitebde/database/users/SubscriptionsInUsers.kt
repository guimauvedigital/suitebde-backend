package com.suitebde.database.users

import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.users.SubscriptionInUser
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SubscriptionsInUsers : UUIDTable() {

    val userId = uuid("user_id").index()
    val subscriptionId = uuid("subscription_id").index()
    val startsAt = timestamp("starts_at")
    val endsAt = timestamp("ends_at")

    fun toSubscriptionInUser(
        row: ResultRow,
        subscriptionInAssociation: SubscriptionInAssociation?,
    ) = SubscriptionInUser(
        UUID(row[id].value),
        UUID(row[userId]),
        UUID(row[subscriptionId]),
        row[startsAt],
        row[endsAt],
        subscriptionInAssociation
    )

}
