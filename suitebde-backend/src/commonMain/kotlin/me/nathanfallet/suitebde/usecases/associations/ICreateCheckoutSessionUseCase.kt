package me.nathanfallet.suitebde.usecases.associations

import com.stripe.model.checkout.Session
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.usecases.base.IQuadSuspendUseCase

interface ICreateCheckoutSessionUseCase : IQuadSuspendUseCase<Association, String, Long, String, Session?>
