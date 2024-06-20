package com.suitebde.repositories.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateClubPayload
import com.suitebde.models.clubs.UpdateClubPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IClubsRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<Club>
    suspend fun get(id: UUID, associationId: UUID): Club?
    suspend fun create(payload: CreateClubPayload, associationId: UUID): Club?
    suspend fun update(id: UUID, payload: UpdateClubPayload, associationId: UUID): Club?
    suspend fun delete(id: UUID, associationId: UUID): Boolean

}
