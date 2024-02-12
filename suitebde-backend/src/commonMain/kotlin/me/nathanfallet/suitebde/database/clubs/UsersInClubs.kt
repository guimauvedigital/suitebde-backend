package me.nathanfallet.suitebde.database.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.users.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.alias

object UsersInClubs : Table() {

    val userId = varchar("user_id", 32).index()
    val clubId = varchar("club_id", 32).index()
    val roleId = varchar("role_id", 32).index()

    val isMember = alias("IsMember")

    override val primaryKey = PrimaryKey(arrayOf(clubId, userId))

    fun toUserInClub(
        row: ResultRow,
        user: User? = null,
        club: Club? = null,
        role: RoleInClub,
    ) = UserInClub(
        row[userId],
        row[clubId],
        row[roleId],
        user,
        club,
        role
    )

}
