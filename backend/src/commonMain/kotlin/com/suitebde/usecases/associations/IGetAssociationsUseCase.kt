package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetAssociationsUseCase : ISuspendUseCase<Boolean, List<Association>>
