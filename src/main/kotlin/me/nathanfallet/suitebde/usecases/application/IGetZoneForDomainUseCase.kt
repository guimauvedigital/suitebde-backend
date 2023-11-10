package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.cloudflare.models.zones.Zone
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetZoneForDomainUseCase : ISuspendUseCase<String, Zone?>
