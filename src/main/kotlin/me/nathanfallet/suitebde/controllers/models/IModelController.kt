package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.models.ModelKey

interface IModelController<T, P, Q> {

    val modelKeys: List<ModelKey>

    suspend fun getAll(call: ApplicationCall): List<T>
    suspend fun get(call: ApplicationCall, id: String): T
    suspend fun create(call: ApplicationCall, payload: P): T
    suspend fun update(call: ApplicationCall, id: String, payload: Q): T
    suspend fun delete(call: ApplicationCall, id: String)

}