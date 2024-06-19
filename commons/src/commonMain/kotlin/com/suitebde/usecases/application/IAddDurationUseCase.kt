package com.suitebde.usecases.application

import dev.kaccelero.usecases.IPairUseCase
import kotlinx.datetime.Instant

interface IAddDurationUseCase : IPairUseCase<Instant, String, Instant>
