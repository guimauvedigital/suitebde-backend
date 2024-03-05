package me.nathanfallet.suitebde.repositories.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.usecases.pagination.Pagination

interface IClubsRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: String): List<Club>
    suspend fun get(id: String, associationId: String): Club?
    suspend fun create(payload: CreateClubPayload, associationId: String): Club?
    suspend fun update(id: String, payload: UpdateClubPayload, associationId: String): Club?
    suspend fun delete(id: String, associationId: String): Boolean

}
