package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object CodesInEmails : Table() {

    val email = varchar("email", 255)
    val code = varchar("code", 32)
    val associationId = varchar("association_id", 32).nullable()
    val expiresAt = varchar("expires_at", 255)

    override val primaryKey = PrimaryKey(email)

    fun toCodeInEmail(
        row: ResultRow,
    ) = me.nathanfallet.suitebde.models.associations.CodeInEmail(
        row[email],
        row[code],
        row.getOrNull(associationId),
        row[expiresAt].let(Instant::parse)
    )

}
