package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface ICreateStripeAccountLinkUseCase : IPairSuspendUseCase<Association, String, String?>
