package com.suitebde.repositories.associations

import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IDomainsInAssociationsRepository :
    IChildModelSuspendRepository<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, UUID>
