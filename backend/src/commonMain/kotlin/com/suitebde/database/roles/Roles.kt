package com.suitebde.database.roles

import com.suitebde.models.roles.Role
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object Roles : UUIDTable() {

    val associationId = uuid("association_id").index()
    val name = varchar("name", 255)

    fun toRole(
        row: ResultRow,
    ) = Role(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[name]
    )

}
