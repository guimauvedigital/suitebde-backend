package com.suitebde.database.roles

import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UserInRole
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object UsersInRoles : Table() {

    val roleId = uuid("role_id").index()
    val userId = uuid("user_id").index()

    override val primaryKey = PrimaryKey(arrayOf(roleId, userId))

    fun toUserInRole(
        row: ResultRow,
        role: Role? = null,
        user: User? = null,
    ) = UserInRole(
        UUID(row[roleId]),
        UUID(row[userId]),
        role,
        user
    )

}
