package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.StripeAccount
import dev.kaccelero.usecases.ISuspendUseCase

interface IRefreshStripeAccountUseCase : ISuspendUseCase<Association, List<StripeAccount>>
