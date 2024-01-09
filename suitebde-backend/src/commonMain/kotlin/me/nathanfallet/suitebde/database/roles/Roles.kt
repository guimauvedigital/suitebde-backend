package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.roles.Role
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object Roles : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toRole(
        row: ResultRow,
    ) = Role(
        row[id],
        row[associationId],
        row[name]
    )

}
