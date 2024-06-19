package com.suitebde.database.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.alias

object UsersInClubs : Table() {

    val userId = uuid("user_id").index()
    val clubId = uuid("club_id").index()
    val roleId = uuid("role_id").index()

    val isMember = alias("IsMember")

    override val primaryKey = PrimaryKey(arrayOf(clubId, userId))

    fun toUserInClub(
        row: ResultRow,
        user: User? = null,
        club: Club? = null,
        role: RoleInClub,
    ) = UserInClub(
        UUID(row[userId]),
        UUID(row[clubId]),
        UUID(row[roleId]),
        user,
        club,
        role
    )

}
