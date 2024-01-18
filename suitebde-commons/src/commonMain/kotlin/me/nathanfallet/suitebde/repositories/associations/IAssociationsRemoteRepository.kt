package me.nathanfallet.suitebde.repositories.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

interface IAssociationsRemoteRepository {

    suspend fun list(limit: Long, offset: Long): List<Association>
    suspend fun get(id: String): Association?
    suspend fun update(id: String, payload: UpdateAssociationPayload): Association?

}
