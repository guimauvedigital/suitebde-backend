package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface IExpireUseCase : ISuspendUseCase<Instant, Unit>