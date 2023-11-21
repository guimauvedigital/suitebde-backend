package me.nathanfallet.suitebde.repositories.associations

import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IDomainsInAssociationsRepository :
    IChildModelSuspendRepository<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, String>
