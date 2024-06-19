package com.suitebde.database.clubs

import com.suitebde.models.clubs.RoleInClub
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object RolesInClubs : UUIDTable() {

    val clubId = uuid("club_id").index()
    val name = varchar("name", 255)
    val admin = bool("admin")
    val default = bool("default")

    fun toRoleInClub(
        row: ResultRow,
    ) = RoleInClub(
        UUID(row[id].value),
        UUID(row[clubId]),
        row[name],
        row[admin],
        row[default],
    )

}
