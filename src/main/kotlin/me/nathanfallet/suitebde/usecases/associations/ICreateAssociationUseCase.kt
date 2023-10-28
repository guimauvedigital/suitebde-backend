package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ICreateAssociationUseCase : ISuspendUseCase<Pair<CreateAssociationPayload, Instant>, Association?>