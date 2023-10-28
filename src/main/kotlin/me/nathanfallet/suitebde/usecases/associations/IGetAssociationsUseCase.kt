package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface IGetAssociationsUseCase : ISuspendUseCase<Boolean, List<Association>>