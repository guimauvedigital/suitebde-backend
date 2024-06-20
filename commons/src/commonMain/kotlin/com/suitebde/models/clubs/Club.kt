package com.suitebde.models.clubs

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Club(
    @ModelProperty("id")
    @Schema("Id of the club", "123abc")
    override val id: UUID,
    @Schema("Id of the association the club is in", "123abc")
    val associationId: UUID,
    @ModelProperty("string")
    @Schema("Name of the club", "Club Informatique")
    val name: String,
    @Schema("Description of the club", "Le club informatique est un club qui s'occupe de tout ce qui est informatique.")
    val description: String,
    @ModelProperty("image")
    @Schema("Logo of the club", "https://example.com/logo.png")
    val logo: String?,
    @Schema("Creation date of the club", "2023-12-14T09:41:00Z")
    val createdAt: Instant,
    @ModelProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean,
    @Schema("Number of members in the club", "42")
    val usersCount: Long,
    @Schema("Is current user a member of the club?", "true")
    val isMember: Boolean?,
) : IChildModel<UUID, CreateClubPayload, UpdateClubPayload, UUID> {

    override val parentId: UUID
        get() = associationId

}
