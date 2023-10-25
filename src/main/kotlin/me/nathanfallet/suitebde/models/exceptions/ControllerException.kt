package me.nathanfallet.suitebde.models.exceptions

import io.ktor.http.*

class ControllerException(
    val code: HttpStatusCode,
    override val message: String
) : Exception(message)