package com.suitebde.database.users

import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Users : UUIDTable() {

    val associationId = uuid("association_id").index()
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val superuser = bool("superuser")
    val lastLoginAt = timestamp("last_login_at")

    fun toUser(
        row: ResultRow,
        includePassword: Boolean = false,
    ) = User(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[email],
        row[password].takeIf { includePassword },
        row[firstName],
        row[lastName],
        row[superuser],
        row[lastLoginAt]
    )

}
