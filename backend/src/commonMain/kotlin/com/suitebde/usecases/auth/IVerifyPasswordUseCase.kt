package com.suitebde.usecases.auth

import dev.kaccelero.usecases.IPairUseCase

interface IVerifyPasswordUseCase : IPairUseCase<String, String, Boolean>
