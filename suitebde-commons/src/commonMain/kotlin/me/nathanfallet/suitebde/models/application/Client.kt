package me.nathanfallet.suitebde.models.application

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.auth.IClient
import me.nathanfallet.usecases.models.IModel

@Serializable
data class Client(
    override val id: String,
    val ownerId: String,
    val name: String,
    val description: String,
    val secret: String,
    override val redirectUri: String,
) : IClient, IModel<String, Unit, Unit> {

    override val clientId = id
    override val clientSecret = secret

}
