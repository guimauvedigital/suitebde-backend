package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetDomainsInAssociationsUseCase : ISuspendUseCase<String, List<DomainInAssociation>>

