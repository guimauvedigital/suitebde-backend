package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface IGetCodeInEmailUseCase : ISuspendUseCase<Pair<String, Instant>, CodeInEmail?>