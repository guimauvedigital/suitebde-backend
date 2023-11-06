package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.usecases.base.pair.IPairUseCase

interface IVerifyPasswordUseCase : IPairUseCase<String, String, Boolean>