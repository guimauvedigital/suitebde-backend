package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.usecases.pair.IPairSuspendUseCase

interface IGetCodeInEmailUseCase : IPairSuspendUseCase<String, Instant, CodeInEmail?>