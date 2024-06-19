package com.suitebde.repositories.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.models.UUID

interface IAssociationsRemoteRepository {

    suspend fun list(): List<Association>
    suspend fun get(id: UUID): Association?
    suspend fun update(id: UUID, payload: UpdateAssociationPayload): Association?

}
