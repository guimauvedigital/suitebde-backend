package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.usecases.pair.IPairUseCase

interface IVerifyPasswordUseCase : IPairUseCase<String, String, Boolean>