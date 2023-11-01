package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.usecases.ISuspendUseCase

interface IGetAssociationUseCase : ISuspendUseCase<String, Association?>