package com.suitebde.models.auth

import com.suitebde.models.application.Client
import com.suitebde.models.users.User
import kotlinx.serialization.Serializable

@Serializable
data class ClientForUser(
    val client: Client,
    val user: User,
)
