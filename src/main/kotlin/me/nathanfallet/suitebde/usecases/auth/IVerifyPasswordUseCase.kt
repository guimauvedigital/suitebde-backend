package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.usecases.IUseCase

interface IVerifyPasswordUseCase : IUseCase<Pair<String, String>, Boolean>