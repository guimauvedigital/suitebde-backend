package me.nathanfallet.suitebde.models.clubs

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class Club(
    @ModelProperty("id")
    @Schema("Id of the club", "123abc")
    override val id: String,
    @Schema("Id of the association the club is in", "123abc")
    val associationId: String,
    @ModelProperty("string")
    @Schema("Name of the club", "Club Informatique")
    val name: String,
    @ModelProperty("string")
    @Schema("Description of the club", "Le club informatique est un club qui s'occupe de tout ce qui est informatique.")
    val description: String,
    @ModelProperty("string")
    @Schema("Icon of the club", "https://example.com/icon.png")
    val icon: String?,
    @Schema("Creation date of the club", "2023-12-14T09:41:00Z")
    val createdAt: Instant,
    @ModelProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean,
) : IChildModel<String, CreateClubPayload, UpdateClubPayload, String> {

    override val parentId: String
        get() = associationId

}
