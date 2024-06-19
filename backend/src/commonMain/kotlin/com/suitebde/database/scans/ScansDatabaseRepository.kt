package com.suitebde.database.scans

import com.suitebde.database.users.Users
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.models.users.UserContext
import com.suitebde.repositories.scans.IScansRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.exposed.sql.*

class ScansDatabaseRepository(
    private val database: IDatabase,
) : IScansRepository {

    init {
        database.transaction {
            SchemaUtils.create(Scans)
        }
    }

    override suspend fun create(payload: CreateScanPayload, parentId: UUID, context: IContext?): Scan? {
        if (context !is UserContext) return null
        return database.suspendedTransaction {
            Scans.insert {
                it[associationId] = parentId
                it[userId] = payload.userId
                it[scannerId] = context.userId
                it[scannedAt] = Clock.System.now()
            }.resultedValues?.map(Scans::toScan)?.singleOrNull()
        }
    }

    override suspend fun listBetween(parentId: UUID, startsAt: LocalDate, endsAt: LocalDate): List<Scan> =
        database.suspendedTransaction {
            Scans
                .join(Users, JoinType.LEFT, Scans.userId, Users.id)
                .selectAll()
                .where {
                    Scans.associationId eq parentId and
                            (Scans.scannedAt greaterEq startsAt.atStartOfDayIn(TimeZone.currentSystemDefault())) and
                            (Scans.scannedAt lessEq endsAt.atStartOfDayIn(TimeZone.currentSystemDefault()))
                }
                .map { Scans.toScan(it, Users.toUser(it)) }
        }

}
