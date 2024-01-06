package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class DomainInAssociation(
    @ModelProperty("id")
    @Schema("Domain name linked to the association", "bdensisa.org")
    val domain: String,
    @Schema("Id of the association", "123abc")
    val associationId: String,
) : IChildModel<String, CreateDomainInAssociationPayload, Unit, String> {

    override val id = domain
    override val parentId = associationId

}
