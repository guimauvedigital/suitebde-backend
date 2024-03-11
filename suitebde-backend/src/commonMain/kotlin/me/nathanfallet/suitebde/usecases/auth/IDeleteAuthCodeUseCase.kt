package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.usecases.base.ISuspendUseCase

interface IDeleteAuthCodeUseCase : ISuspendUseCase<String, Boolean>
