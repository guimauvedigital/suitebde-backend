package me.nathanfallet.suitebde.database.users

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.users.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select

object Users : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val superuser = bool("superuser")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (select { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toUser(
        row: ResultRow,
        includePassword: Boolean = false
    ) = User(
        row[id],
        row[associationId],
        row[email],
        row[password].takeIf { includePassword },
        row[firstName],
        row[lastName],
        row[superuser]
    )

}