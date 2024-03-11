package me.nathanfallet.suitebde.models.auth

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.users.User

@Serializable
data class ClientForUser(
    val client: Client,
    val user: User,
)
