package me.nathanfallet.suitebde.models.users

import me.nathanfallet.usecases.context.IContext

data class OptionalUserContext(
    val userId: String? = null,
) : IContext
