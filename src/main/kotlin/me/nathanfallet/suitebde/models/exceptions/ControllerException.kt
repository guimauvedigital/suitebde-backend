package me.nathanfallet.suitebde.models.exceptions

import io.ktor.http.*

class ControllerException(
    val code: HttpStatusCode,
    val key: String
) : Exception()