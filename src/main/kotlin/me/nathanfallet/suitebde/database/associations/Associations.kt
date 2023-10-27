package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.toInstant
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.utils.IdHelper
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select

object Associations : Table() {

    val id = varchar("id", 32)
    val name = varchar("name", 255)
    val school = varchar("school", 255)
    val city = varchar("city", 255)
    val validated = bool("validated")
    val createdAt = varchar("created_at", 255)
    val expiresAt = varchar("expires_at", 255)

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = IdHelper.generateId()
        return if (select { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toAssociation(
        row: ResultRow
    ) = Association(
        row[id],
        row[name],
        row[school],
        row[city],
        row[validated],
        row[createdAt].toInstant(),
        row[expiresAt].toInstant()
    )

}