package com.suitebde.models.scans

import com.suitebde.models.users.User
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Scan(
    @Schema("Id of the scan entry", "123abc")
    override val id: UUID,
    @Schema("Id of the association the entry is in", "123abc")
    val associationId: UUID,
    @Schema("Id of the user that has been scanned", "123abc")
    val userId: UUID,
    @Schema("Id of the user that scanned the user", "123abc")
    val scannerId: UUID,
    @Schema("Date the user has been scanned at", "2023-12-13T09:41:00Z")
    val scannedAt: Instant,
    @Schema("User that has been scanned", "{...}")
    val user: User? = null,
    @Schema("User that scanned the user", "{...}")
    val scanner: User? = null,
) : IChildModel<UUID, CreateScanPayload, Unit, UUID> {

    override val parentId: UUID
        get() = associationId

}
