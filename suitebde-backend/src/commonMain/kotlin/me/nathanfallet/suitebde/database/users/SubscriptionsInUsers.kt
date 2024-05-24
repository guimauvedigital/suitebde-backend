package me.nathanfallet.suitebde.database.users

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object SubscriptionsInUsers : Table() {

    val id = varchar("id", 32)
    val userId = varchar("user_id", 32).index()
    val subscriptionId = varchar("subscription_id", 32).index()
    val startsAt = varchar("starts_at", 255)
    val endsAt = varchar("ends_at", 255)

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toSubscriptionInUser(
        row: ResultRow,
        subscriptionInAssociation: SubscriptionInAssociation?,
    ) = SubscriptionInUser(
        row[id],
        row[userId],
        row[subscriptionId],
        row[startsAt].let(Instant::parse),
        row[endsAt].let(Instant::parse),
        subscriptionInAssociation
    )

}
