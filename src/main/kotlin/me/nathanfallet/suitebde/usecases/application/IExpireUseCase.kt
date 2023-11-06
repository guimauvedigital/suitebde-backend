package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IExpireUseCase : ISuspendUseCase<Instant, Unit>