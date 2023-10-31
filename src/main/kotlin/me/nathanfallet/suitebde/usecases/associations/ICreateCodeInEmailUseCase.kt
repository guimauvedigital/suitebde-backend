package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.usecases.triple.ITripleSuspendUseCase

interface ICreateCodeInEmailUseCase : ITripleSuspendUseCase<String, String?, Instant, CodeInEmail?>