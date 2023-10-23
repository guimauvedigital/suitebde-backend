package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.utils.IdHelper
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select

object Associations : Table() {

    val id = varchar("id", 32)
    val name = varchar("name", 255)

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = IdHelper.generateId()
        return if (select { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toAssociation(
        row: ResultRow
    ) = Association(
        row[id],
        row[name]
    )

}