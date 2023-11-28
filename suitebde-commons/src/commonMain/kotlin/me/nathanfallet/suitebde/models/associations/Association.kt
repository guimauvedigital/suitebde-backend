package me.nathanfallet.suitebde.models.associations

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.annotations.ModelProperty

@Serializable
data class Association(
    @ModelProperty("id")
    override val id: String,
    @ModelProperty("string")
    val name: String,
    @ModelProperty("string")
    val school: String,
    @ModelProperty("string")
    val city: String,
    @ModelProperty("boolean")
    val validated: Boolean,
    val createdAt: Instant,
    val expiresAt: Instant
) : IModel<String, me.nathanfallet.suitebde.models.associations.CreateAssociationPayload, me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload>
