package com.suitebde.database.associations

import com.suitebde.models.associations.Association
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Associations : UUIDTable() {

    val name = varchar("name", 255)
    val school = varchar("school", 255)
    val city = varchar("city", 255)
    val validated = bool("validated")
    val createdAt = timestamp("created_at")
    val expiresAt = timestamp("expires_at")

    fun toAssociation(
        row: ResultRow,
    ) = Association(
        UUID(row[id].value),
        row[name],
        row[school],
        row[city],
        row[validated],
        row[createdAt],
        row[expiresAt]
    )

}
