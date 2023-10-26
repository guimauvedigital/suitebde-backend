package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.suitebde.controllers.IController
import me.nathanfallet.suitebde.models.associations.Association

interface IAssociationController : IController {

    suspend fun getAll(): List<Association>

}