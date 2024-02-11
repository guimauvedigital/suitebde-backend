package me.nathanfallet.suitebde.database.clubs

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
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
            customJoin()
                .where { Clubs.associationId eq parentId }
                .groupBy(Clubs.id)
                .map(Clubs::toClub)
        }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<Club> =
        database.suspendedTransaction {
            customJoin()
                .where { Clubs.associationId eq parentId }
                .groupBy(Clubs.id)
                .limit(limit.toInt(), offset)
                .map(Clubs::toClub)
        }

    override suspend fun create(payload: CreateClubPayload, parentId: String, context: IContext?): Club? {
        val id = database.suspendedTransaction {
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
        }
        return get(id, parentId, context)
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): Club? =
        database.suspendedTransaction {
            customJoin()
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

    private fun customJoin(): Query =
        Clubs
            .join(UsersInClubs, JoinType.LEFT, Clubs.id, UsersInClubs.clubId)
            .select(Clubs.columns + Clubs.usersCount)

}
