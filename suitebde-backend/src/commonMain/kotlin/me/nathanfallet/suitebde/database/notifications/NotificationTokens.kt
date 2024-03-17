package me.nathanfallet.suitebde.database.notifications

import me.nathanfallet.suitebde.models.notifications.NotificationToken
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object NotificationTokens : Table() {

    val token = varchar("token", 255)
    val userId = varchar("user_id", 32)

    override val primaryKey = PrimaryKey(token)

    fun toNotificationToken(
        row: ResultRow,
    ) = NotificationToken(
        row[token],
        row[userId]
    )

}
