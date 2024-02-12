package me.nathanfallet.suitebde.database.clubs

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object RolesInClubs : Table() {

    val id = varchar("id", 32)
    val clubId = varchar("club_id", 32).index()
    val name = varchar("name", 255)
    val admin = bool("admin")
    val default = bool("default")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toRoleInClub(
        row: ResultRow,
    ) = RoleInClub(
        row[id],
        row[clubId],
        row[name],
        row[admin],
        row[default],
    )

}
