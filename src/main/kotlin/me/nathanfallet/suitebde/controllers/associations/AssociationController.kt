package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase

class AssociationController(
    private val getAssociationsUseCase: IGetAssociationsUseCase
) : IAssociationController {

    override suspend fun getAll(): List<Association> {
        return getAssociationsUseCase(true)
    }

}