package com.suitebde.database.notifications

import com.suitebde.models.notifications.NotificationToken
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object NotificationTokens : Table() {

    val token = varchar("token", 255)
    val userId = uuid("user_id")

    override val primaryKey = PrimaryKey(token)

    fun toNotificationToken(
        row: ResultRow,
    ) = NotificationToken(
        row[token],
        UUID(row[userId])
    )

}
