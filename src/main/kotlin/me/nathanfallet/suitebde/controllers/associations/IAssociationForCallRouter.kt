package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.ktorx.routers.IModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

interface IAssociationForCallRouter :
    IModelRouter<Association, String, CreateAssociationPayload, UpdateAssociationPayload>
