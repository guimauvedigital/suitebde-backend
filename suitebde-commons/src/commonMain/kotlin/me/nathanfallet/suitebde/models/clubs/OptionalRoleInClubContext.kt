package me.nathanfallet.suitebde.models.clubs

import me.nathanfallet.usecases.context.IContext

data class OptionalRoleInClubContext(
    val roleId: String? = null,
) : IContext
