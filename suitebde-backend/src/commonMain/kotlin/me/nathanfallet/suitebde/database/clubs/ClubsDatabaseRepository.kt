package me.nathanfallet.suitebde.database.clubs

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.application.SearchOptions
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.ClubContext
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.pagination.IPaginationOptions
import me.nathanfallet.usecases.pagination.Pagination
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ClubsDatabaseRepository(
    private val database: IDatabase,
) : IClubsRepository {

    init {
        database.transaction {
            SchemaUtils.create(Clubs)
            SchemaUtils.create(UsersInClubs)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<Club> =
        database.suspendedTransaction {
            customJoin((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .where { Clubs.associationId eq parentId }
                .andWhere(context as? ClubContext)
                .groupBy(Clubs.id)
                .orderByMemberAndName((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .map(Clubs::toClub)
        }

    override suspend fun list(pagination: Pagination, parentId: String, context: IContext?): List<Club> =
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

    override suspend fun create(payload: CreateClubPayload, parentId: String, context: IContext?): Club? =
        database.suspendedTransaction {
            val id = Clubs.generateId()
            Clubs.insert {
                it[Clubs.id] = id
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[logo] = payload.logo
                it[createdAt] = Clock.System.now().toString()
                it[validated] = payload.validated ?: false
            }
            id
        }.let { id -> get(id, parentId, context) }

    override suspend fun get(id: String, parentId: String, context: IContext?): Club? =
        database.suspendedTransaction {
            customJoin((context as? ClubContext)?.userId ?: (context as? OptionalUserContext)?.userId)
                .where { Clubs.id eq id and (Clubs.associationId eq parentId) }
                .groupBy(Clubs.id)
                .map(Clubs::toClub)
                .singleOrNull()
        }

    override suspend fun update(id: String, payload: UpdateClubPayload, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            Clubs.update({ Clubs.id eq id and (Clubs.associationId eq parentId) }) {
                payload.name?.let { name -> it[Clubs.name] = name }
                payload.description?.let { description -> it[Clubs.description] = description }
                payload.logo?.let { logo -> it[Clubs.logo] = logo }
                payload.validated?.let { validated -> it[Clubs.validated] = validated }
            }
        } == 1

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            Clubs.deleteWhere {
                Clubs.id eq id and (associationId eq parentId)
            }
        } == 1

    private fun customJoin(viewedBy: String?): Query {
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

    private fun Query.orderByMemberAndName(viewedBy: String?): Query =
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

    private fun Query.andWhere(context: ClubContext?): Query = context?.takeIf { it.showValidated }?.let {
        andWhere { Clubs.validated eq true }
    } ?: this

}
