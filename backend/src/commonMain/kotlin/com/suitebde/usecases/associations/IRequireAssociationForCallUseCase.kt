package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import dev.kaccelero.usecases.ISuspendUseCase
import io.ktor.server.application.*

interface IRequireAssociationForCallUseCase : ISuspendUseCase<ApplicationCall, Association>
