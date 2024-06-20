package com.suitebde.usecases.auth

import com.suitebde.models.auth.ClientForUser
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetAuthCodeUseCase : ISuspendUseCase<String, ClientForUser?>
