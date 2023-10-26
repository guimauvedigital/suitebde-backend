package me.nathanfallet.suitebde.models.exceptions

import io.ktor.http.*
import me.nathanfallet.suitebde.models.LocalizedString

class ControllerException(
    val code: HttpStatusCode,
    val error: LocalizedString
) : Exception(error.value)