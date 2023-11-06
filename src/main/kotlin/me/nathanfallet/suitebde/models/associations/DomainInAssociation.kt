package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IModel

@Serializable
data class DomainInAssociation(
    val domain: String,
    val associationId: String
) : IModel<String, Unit, Unit> {

    override val id: String
        get() = domain

}
