package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.suitebde.models.associations.Association

interface IAssociationController {

    suspend fun getAll(): List<Association>

}