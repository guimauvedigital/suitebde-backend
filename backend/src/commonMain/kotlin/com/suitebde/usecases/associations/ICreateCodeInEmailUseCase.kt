package com.suitebde.usecases.associations

import com.suitebde.models.associations.CodeInEmail
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface ICreateCodeInEmailUseCase : IPairSuspendUseCase<String, UUID?, CodeInEmail?>
