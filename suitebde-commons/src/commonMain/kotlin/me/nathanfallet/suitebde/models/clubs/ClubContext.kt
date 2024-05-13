package me.nathanfallet.suitebde.models.clubs

import me.nathanfallet.usecases.context.IContext

data class ClubContext(
    val userId: String? = null,
    val onlyShowValidated: Boolean = true,
) : IContext
