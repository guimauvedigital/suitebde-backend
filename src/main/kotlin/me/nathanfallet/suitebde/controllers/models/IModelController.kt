package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*

interface IModelController<T, P, Q> {

    suspend fun getAll(call: ApplicationCall): List<T>
    suspend fun get(call: ApplicationCall): T
    suspend fun create(call: ApplicationCall, payload: P): T
    suspend fun update(call: ApplicationCall, payload: Q): T
    suspend fun delete(call: ApplicationCall)

}