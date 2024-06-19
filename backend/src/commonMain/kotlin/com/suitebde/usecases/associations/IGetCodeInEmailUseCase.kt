package com.suitebde.usecases.associations

import com.suitebde.models.associations.CodeInEmail
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetCodeInEmailUseCase : ISuspendUseCase<String, CodeInEmail?>
