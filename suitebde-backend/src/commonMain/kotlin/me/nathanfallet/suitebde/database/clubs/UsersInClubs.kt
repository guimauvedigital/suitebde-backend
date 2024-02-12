package me.nathanfallet.suitebde.database.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.users.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.alias

object UsersInClubs : Table() {

    val clubId = varchar("club_id", 32).index()
    val userId = varchar("user_id", 32).index()

    val isMember = alias("IsMember")

    override val primaryKey = PrimaryKey(arrayOf(clubId, userId))

    fun toUserInClub(
        row: ResultRow,
        club: Club? = null,
        user: User? = null,
    ) = UserInClub(
        row[clubId],
        row[userId],
        club,
        user
    )

}
