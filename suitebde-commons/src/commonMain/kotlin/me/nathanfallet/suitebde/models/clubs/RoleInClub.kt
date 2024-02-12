package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class RoleInClub(
    @ModelProperty("id")
    @Schema("Id of the role", "123abc")
    override val id: String,
    @Schema("Id of the club the role is in", "123abc")
    val clubId: String,
    @ModelProperty("string")
    @Schema("Name of the role", "President")
    val name: String,
    @ModelProperty("boolean")
    @Schema("Is the role an admin role?", "true")
    val admin: Boolean,
    @ModelProperty("boolean")
    @Schema("Is the role a default role?", "true")
    val default: Boolean = false,
) : IChildModel<String, CreateRoleInClubPayload, UpdateRoleInClubPayload, String> {

    override val parentId: String
        get() = clubId

}
