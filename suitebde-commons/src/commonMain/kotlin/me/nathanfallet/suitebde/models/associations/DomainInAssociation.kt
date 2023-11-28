package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty

@Serializable
data class DomainInAssociation(
    @ModelProperty("id")
    val domain: String,
    val associationId: String
) : IChildModel<String, me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload, Unit, String> {

    override val id: String
        get() = domain

    override val parentId: String
        get() = associationId

}
