package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.usecases.base.ISuspendUseCase

interface IShutdownDomainUseCase : ISuspendUseCase<String, Boolean>

