package com.suitebde.usecases.application

import dev.kaccelero.usecases.ISuspendUseCase
import me.nathanfallet.cloudflare.models.zones.Zone

interface IGetZoneForDomainUseCase : ISuspendUseCase<String, Zone?>
