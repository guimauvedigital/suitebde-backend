package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.usecases.base.IPairUseCase

interface IAddDurationUseCase : IPairUseCase<Instant, String, Instant>
