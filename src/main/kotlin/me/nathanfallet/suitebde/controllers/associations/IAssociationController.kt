package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

interface IAssociationController : IModelController<Association, Unit, UpdateAssociationPayload>