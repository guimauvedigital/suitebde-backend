package com.suitebde.usecases.stripe

import com.stripe.model.checkout.Session
import com.suitebde.models.stripe.StripeOrder
import dev.kaccelero.usecases.ISuspendUseCase

interface ICreateStripeOrderForSessionUseCase : ISuspendUseCase<Session, StripeOrder?>
