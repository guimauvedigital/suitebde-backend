package com.suitebde.models.associations

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class DomainInAssociation(
    @ModelProperty("id")
    @Schema("Domain name linked to the association", "bdensisa.org")
    val domain: String,
    @Schema("Id of the association", "123abc")
    val associationId: UUID,
) : IChildModel<String, CreateDomainInAssociationPayload, Unit, UUID> {

    override val id: String
        get() = domain

    override val parentId: UUID
        get() = associationId

}
