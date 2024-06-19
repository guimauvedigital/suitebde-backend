package com.suitebde.database.clubs

import com.suitebde.models.clubs.Club
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.countDistinct
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Clubs : UUIDTable() {

    val associationId = uuid("association_id").index()
    val name = text("name")
    val description = text("description")
    val logo = text("logo").nullable()
    val createdAt = timestamp("created_at")
    val validated = bool("validated")
    val usersCount = UsersInClubs.userId.countDistinct()
    val isMember = UsersInClubs.isMember[UsersInClubs.userId].countDistinct()

    fun toClub(
        row: ResultRow,
    ) = Club(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[name],
        row[description],
        row[logo],
        row[createdAt],
        row[validated],
        row[usersCount],
        row.getOrNull(isMember)?.let { it >= 1L }
    )

}
