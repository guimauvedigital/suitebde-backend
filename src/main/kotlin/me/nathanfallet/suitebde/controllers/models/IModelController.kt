package me.nathanfallet.suitebde.controllers.models

import me.nathanfallet.suitebde.controllers.IController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.User

interface IModelController<T, P, Q> : IController {

    suspend fun getAll(association: Association, user: User?): List<T>
    suspend fun get(association: Association, user: User?, id: String): T
    suspend fun create(association: Association, user: User?, payload: P): T
    suspend fun update(association: Association, user: User?, id: String, payload: Q): T
    suspend fun delete(association: Association, user: User?, id: String)

}