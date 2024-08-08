package com.suitebde.database.users

import com.suitebde.extensions.generateId
import com.suitebde.models.users.ResetInUser
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll

object ResetsInUsers : Table() {

    val code = varchar("code", 32)
    val userId = uuid("user_id")
    val expiration = timestamp("expiration")

    override val primaryKey = PrimaryKey(code)

    fun generateCode(): String {
        val candidate = String.generateId()
        return if (selectAll().where { code eq candidate }.count() > 0) generateCode() else candidate
    }

    fun toResetInUser(
        row: ResultRow,
    ) = ResetInUser(
        row[code],
        UUID(row[userId]),
        row[expiration]
    )

}
