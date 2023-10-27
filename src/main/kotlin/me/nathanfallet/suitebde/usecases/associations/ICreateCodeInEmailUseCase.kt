package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ICreateCodeInEmailUseCase : ISuspendUseCase<Pair<String, String?>, CodeInEmail?>