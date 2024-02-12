package me.nathanfallet.suitebde.models.users

import me.nathanfallet.usecases.context.IContext

data class UserContext(
    val userId: String,
) : IContext
