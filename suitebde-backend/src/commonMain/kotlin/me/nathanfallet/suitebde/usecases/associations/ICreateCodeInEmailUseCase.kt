package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface ICreateCodeInEmailUseCase : IPairSuspendUseCase<String, String?, CodeInEmail?>
