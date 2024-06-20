package com.suitebde.models.clubs

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class CreateClubPayload(
    @PayloadProperty("string")
    @Schema("Name of the club", "Club Informatique")
    val name: String,
    @PayloadProperty("string")
    @Schema("Description of the club", "Le club informatique est un club qui s'occupe de tout ce qui est informatique.")
    val description: String,
    @PayloadProperty("string")
    @Schema("Logo of the club", "https://example.com/logo.png")
    val logo: String? = null,
    @PayloadProperty("string")
    @Schema("Name of the default member role", "Member")
    val memberRoleName: String,
    @PayloadProperty("string")
    @Schema("Name of the default admin role", "Admin")
    val adminRoleName: String,
    @PayloadProperty("boolean")
    @Schema("Is the club validated?", "true")
    val validated: Boolean? = null,
)
