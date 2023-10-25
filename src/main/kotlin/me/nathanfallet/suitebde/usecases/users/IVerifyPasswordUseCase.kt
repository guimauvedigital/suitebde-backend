package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.usecases.IUseCase

interface IVerifyPasswordUseCase : IUseCase<Pair<String, String>, Boolean>