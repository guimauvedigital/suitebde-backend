package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class DomainInAssociation(
    val domain: String,
    val associationId: String
) : IChildModel<String, String, Unit, String> {

    override val id: String
        get() = domain

    override val parentId: String
        get() = associationId

}
