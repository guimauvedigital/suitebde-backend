package com.suitebde.database.events

import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import com.suitebde.repositories.events.IEventsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.exposed.sql.*

class EventsDatabaseRepository(
    private val database: IDatabase,
) : IEventsRepository {

    init {
        database.transaction {
            SchemaUtils.create(Events)
        }
    }

    override suspend fun listAll(associationId: UUID): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.associationId eq associationId }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .map(Events::toEvent)
        }

    override suspend fun list(parentId: UUID, context: IContext?): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where {
                    Events.associationId eq parentId and Events.validated and (Events.endsAt greaterEq Clock.System.now())
                }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .map(Events::toEvent)
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where {
                    Events.associationId eq parentId and Events.validated and (Events.endsAt greaterEq Clock.System.now())
                }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(Events::toEvent)
        }

    override suspend fun listBetween(associationId: UUID, startsAt: LocalDate, endsAt: LocalDate): List<Event> =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.associationId eq associationId and Events.validated }
                .andWhere {
                    Events.startsAt lessEq endsAt.atStartOfDayIn(TimeZone.currentSystemDefault()) and
                            (Events.endsAt greaterEq startsAt.atStartOfDayIn(TimeZone.currentSystemDefault()))
                }
                .orderBy(Events.startsAt, SortOrder.ASC)
                .map(Events::toEvent)
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): Event? =
        database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.id eq id and (Events.associationId eq parentId) }
                .map(Events::toEvent)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateEventPayload, parentId: UUID, context: IContext?): Event? =
        database.suspendedTransaction {
            Events.insert {
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[image] = payload.image
                it[startsAt] = payload.startsAt
                it[endsAt] = payload.endsAt
                it[validated] = payload.validated ?: false
            }.resultedValues?.map(Events::toEvent)?.singleOrNull()
        }

    override suspend fun update(
        id: UUID,
        payload: UpdateEventPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            Events.update({ Events.id eq id and (Events.associationId eq parentId) }) {
                payload.name?.let { name -> it[Events.name] = name }
                payload.description?.let { description -> it[Events.description] = description }
                payload.image?.let { image -> it[Events.image] = image }
                payload.startsAt?.let { startsAt -> it[Events.startsAt] = startsAt }
                payload.endsAt?.let { endsAt -> it[Events.endsAt] = endsAt }
                payload.validated?.let { validated -> it[Events.validated] = validated }
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Events.deleteWhere {
                Events.id eq id and (associationId eq parentId)
            }
        } == 1

}
