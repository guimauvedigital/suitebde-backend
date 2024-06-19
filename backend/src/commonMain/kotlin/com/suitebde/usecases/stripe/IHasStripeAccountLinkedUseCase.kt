package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import dev.kaccelero.usecases.ISuspendUseCase

interface IHasStripeAccountLinkedUseCase : ISuspendUseCase<Association, Boolean>
