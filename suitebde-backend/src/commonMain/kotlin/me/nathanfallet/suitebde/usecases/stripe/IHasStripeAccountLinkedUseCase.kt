package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IHasStripeAccountLinkedUseCase : ISuspendUseCase<Association, Boolean>
