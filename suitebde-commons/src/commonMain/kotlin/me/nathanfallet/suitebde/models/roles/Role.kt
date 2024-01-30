package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class Role(
    @ModelProperty("id")
    @Schema("Id of the role", "123abc")
    override val id: String,
    @Schema("Id of the association the role is in", "123abc")
    val associationId: String,
    @ModelProperty("string")
    @Schema("Name of the role", "Respo Web")
    val name: String,
) : IChildModel<String, CreateRolePayload, UpdateRolePayload, String> {

    override val parentId: String
        get() = associationId

}
