package me.nathanfallet.suitebde.models.scans

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class Scan(
    @Schema("Id of the scan entry", "123abc")
    override val id: String,
    @Schema("Id of the association the entry is in", "123abc")
    val associationId: String,
    @Schema("Id of the user that has been scanned", "123abc")
    val userId: String,
    @Schema("Id of the user that scanned the user", "123abc")
    val scannerId: String,
    @Schema("Date the user has been scanned at", "2023-12-13T09:41:00Z")
    val scannedAt: Instant,
    @Schema("User that has been scanned", "{...}")
    val user: User? = null,
    @Schema("User that scanned the user", "{...}")
    val scanner: User? = null,
) : IChildModel<String, CreateScanPayload, Unit, String> {

    override val parentId: String
        get() = associationId

}
