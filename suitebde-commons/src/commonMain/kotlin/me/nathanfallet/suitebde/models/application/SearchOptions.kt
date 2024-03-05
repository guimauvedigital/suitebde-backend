package me.nathanfallet.suitebde.models.application

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.pagination.IPaginationOptions

@Serializable
data class SearchOptions(
    val search: String,
) : IPaginationOptions
