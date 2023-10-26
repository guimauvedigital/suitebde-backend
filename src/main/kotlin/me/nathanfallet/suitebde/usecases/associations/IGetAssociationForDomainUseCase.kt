package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface IGetAssociationForDomainUseCase : ISuspendUseCase<String, Association?>