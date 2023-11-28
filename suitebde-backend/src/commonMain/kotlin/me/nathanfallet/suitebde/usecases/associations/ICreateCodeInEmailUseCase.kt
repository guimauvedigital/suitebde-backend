package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.usecases.base.ITripleSuspendUseCase

interface ICreateCodeInEmailUseCase : ITripleSuspendUseCase<String, String?, Instant, CodeInEmail?>