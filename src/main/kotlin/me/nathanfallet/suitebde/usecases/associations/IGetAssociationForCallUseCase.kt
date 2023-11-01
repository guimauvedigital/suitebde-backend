package me.nathanfallet.suitebde.usecases.associations

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.usecases.ISuspendUseCase

interface IGetAssociationForCallUseCase : ISuspendUseCase<ApplicationCall, Association?>