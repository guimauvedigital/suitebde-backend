package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ICreateAssociationUseCase : ISuspendUseCase<CreateAssociationPayload, Association?>