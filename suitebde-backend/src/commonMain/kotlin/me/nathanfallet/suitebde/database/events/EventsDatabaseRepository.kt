package me.nathanfallet.suitebde.database.events

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.pagination.Pagination
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EventsDatabaseRepository(
    private val database: IDatabase,
) : IEventsRepository {

    init {
        database.transaction {
            SchemaUtils.create(Events)
        }
    }

    override suspend fun listAll(associationId: String): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.associationId eq associationId }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .map(Events::toEvent)
        }

    override suspend fun list(parentId: String, context: IContext?): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where {
                    Events.associationId eq parentId and Events.validated and
                            (Events.endsAt greaterEq Clock.System.now().toString())
                }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .map(Events::toEvent)
        }

    override suspend fun list(pagination: Pagination, parentId: String, context: IContext?): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where {
                    Events.associationId eq parentId and Events.validated and
                            (Events.endsAt greaterEq Clock.System.now().toString())
                }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(Events::toEvent)
        }

    override suspend fun listBetween(parentId: String, startsAt: LocalDate, endsAt: LocalDate): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.associationId eq parentId and Events.validated }
                .andWhere { Events.startsAt lessEq endsAt.toString() and (Events.endsAt greaterEq startsAt.toString()) }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .map(Events::toEvent)
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): Event? =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.id eq id and (Events.associationId eq parentId) }
                .map(Events::toEvent)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateEventPayload, parentId: String, context: IContext?): Event? =
        database.suspendedTransaction {
            Events.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[image] = payload.image
                it[startsAt] = payload.startsAt.toString()
                it[endsAt] = payload.endsAt.toString()
                it[validated] = payload.validated ?: false
            }.resultedValues?.map(Events::toEvent)?.singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateEventPayload,
        parentId: String,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            Events.update({ Events.id eq id and (Events.associationId eq parentId) }) {
                payload.name?.let { name -> it[Events.name] = name }
                payload.description?.let { description -> it[Events.description] = description }
                payload.image?.let { image -> it[Events.image] = image }
                payload.startsAt?.let { startsAt -> it[Events.startsAt] = startsAt.toString() }
                payload.endsAt?.let { endsAt -> it[Events.endsAt] = endsAt.toString() }
                payload.validated?.let { validated -> it[Events.validated] = validated }
            }
        } == 1

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            Events.deleteWhere {
                Events.id eq id and (associationId eq parentId)
            }
        } == 1

}
