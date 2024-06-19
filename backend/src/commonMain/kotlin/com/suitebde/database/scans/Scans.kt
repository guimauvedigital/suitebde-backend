package com.suitebde.database.scans

import com.suitebde.models.scans.Scan
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Scans : UUIDTable() {

    val associationId = uuid("association_id").index()
    val userId = uuid("user_id")
    val scannerId = uuid("scanner_id")
    val scannedAt = timestamp("scanned_at")

    fun toScan(
        row: ResultRow,
        user: User? = null,
        scanner: User? = null,
    ) = Scan(
        UUID(row[id].value),
        UUID(row[associationId]),
        UUID(row[userId]),
        UUID(row[scannerId]),
        row[scannedAt],
        user,
        scanner
    )

}
