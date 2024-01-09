package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.suitebde.models.users.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object UsersInRoles : Table() {

    val roleId = varchar("role_id", 32).index()
    val userId = varchar("user_id", 32).index()

    override val primaryKey = PrimaryKey(arrayOf(roleId, userId))

    fun toUserInRole(
        row: ResultRow,
        role: Role? = null,
        user: User? = null,
    ) = UserInRole(
        row[roleId],
        row[userId],
        role,
        user
    )

}
