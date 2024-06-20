package com.suitebde.database.associations

import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object CodesInEmails : Table() {

    val email = varchar("email", 255)
    val code = varchar("code", 32)
    val associationId = uuid("association_id").nullable()
    val expiresAt = timestamp("expires_at")

    override val primaryKey = PrimaryKey(email)

    fun toCodeInEmail(
        row: ResultRow,
    ) = com.suitebde.models.associations.CodeInEmail(
        row[email],
        row[code],
        row.getOrNull(associationId)?.let(::UUID),
        row[expiresAt]
    )

}
