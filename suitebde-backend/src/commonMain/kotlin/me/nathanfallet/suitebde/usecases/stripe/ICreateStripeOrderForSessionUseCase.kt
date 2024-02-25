package me.nathanfallet.suitebde.usecases.stripe

import com.stripe.model.checkout.Session
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ICreateStripeOrderForSessionUseCase : ISuspendUseCase<Session, StripeOrder?>
