package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.IModelRouter

interface IAssociationForCallRouter :
    IModelRouter<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload>
