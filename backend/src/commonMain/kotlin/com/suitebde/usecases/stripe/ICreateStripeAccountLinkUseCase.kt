package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import dev.kaccelero.usecases.IPairSuspendUseCase

interface ICreateStripeAccountLinkUseCase : IPairSuspendUseCase<Association, String, String?>
