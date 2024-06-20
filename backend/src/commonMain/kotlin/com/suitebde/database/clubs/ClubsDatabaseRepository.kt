package com.suitebde.database.clubs

import com.suitebde.models.application.SearchOptions
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.ClubContext
import com.suitebde.models.clubs.CreateClubPayload
import com.suitebde.models.clubs.UpdateClubPayload
import com.suitebde.models.users.OptionalUserContext
import com.suitebde.repositories.clubs.IClubsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IPaginationOptions
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*

class ClubsDatabaseRepository(
    private val database: IDatabase,
) : IClubsRepository {

    init {
        database.transaction {
            SchemaUtils.create(Clubs)
            SchemaUtils.create(UsersInClubs)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<Club> =
        database.suspendedTransaction {
            customJoin((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .where { Clubs.associationId eq parentId }
                .andWhere(context as? ClubContext)
                .groupBy(Clubs.id)
                .orderByMemberAndName((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .map(Clubs::toClub)
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<Club> =
        database.suspendedTransaction {
            customJoin((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .where { Clubs.associationId eq parentId }
                .andWhere(pagination.options)
                .andWhere(context as? ClubContext)
                .groupBy(Clubs.id)
                .orderByMemberAndName((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(Clubs::toClub)
        }

    override suspend fun create(payload: CreateClubPayload, parentId: UUID, context: IContext?): Club? =
        database.suspendedTransaction {
            Clubs.insertAndGetId {
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[logo] = payload.logo
                it[createdAt] = Clock.System.now()
                it[validated] = payload.validated ?: false
            }
        }.let { id -> get(UUID(id.value), parentId, context) }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): Club? =
        database.suspendedTransaction {
            customJoin((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .where { Clubs.id eq id and (Clubs.associationId eq parentId) }
                .groupBy(Clubs.id)
                .map(Clubs::toClub)
                .singleOrNull()
        }

    override suspend fun update(id: UUID, payload: UpdateClubPayload, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Clubs.update({ Clubs.id eq id and (Clubs.associationId eq parentId) }) {
                payload.name?.let { name -> it[Clubs.name] = name }
                payload.description?.let { description -> it[Clubs.description] = description }
                payload.logo?.let { logo -> it[Clubs.logo] = logo }
                payload.validated?.let { validated -> it[Clubs.validated] = validated }
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Clubs.deleteWhere {
                Clubs.id eq id and (associationId eq parentId)
            }
        } == 1

    private fun customJoin(viewedBy: UUID?): Query {
        val columns = Clubs
            .join(UsersInClubs, JoinType.LEFT, Clubs.id, UsersInClubs.clubId)
        val selectedColumns = Clubs.columns + Clubs.usersCount

        val columnsWithMember = viewedBy?.let {
            columns.join(
                UsersInClubs.isMember,
                JoinType.LEFT,
                Clubs.id,
                UsersInClubs.isMember[UsersInClubs.clubId]
            ) { UsersInClubs.isMember[UsersInClubs.userId] eq viewedBy }
        } ?: columns
        val selectedColumnsWithMember = viewedBy?.let {
            selectedColumns + Clubs.isMember
        } ?: selectedColumns

        return columnsWithMember.select(selectedColumnsWithMember)
    }

    private fun Query.orderByMemberAndName(viewedBy: UUID?): Query =
        viewedBy?.let {
            orderBy(Pair(Clubs.isMember, SortOrder.DESC), Pair(Clubs.name, SortOrder.ASC))
        } ?: orderBy(Clubs.name, SortOrder.ASC)

    private fun Query.andWhere(options: IPaginationOptions?): Query = when (options) {
        is SearchOptions -> {
            val likeString = options.search
                .replace("%", "\\%")
                .replace("_", "\\_")
                .let { "%$it%" }
            andWhere { Clubs.name like likeString or (Clubs.description like likeString) }
        }

        else -> this
    }

    private fun Query.andWhere(context: ClubContext?): Query =
        if (context?.onlyShowValidated != false) andWhere { Clubs.validated eq true }
        else this

}
