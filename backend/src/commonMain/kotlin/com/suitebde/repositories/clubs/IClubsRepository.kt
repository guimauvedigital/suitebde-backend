package com.suitebde.repositories.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateClubPayload
import com.suitebde.models.clubs.UpdateClubPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IClubsRepository : IChildModelSuspendRepository<Club, UUID, CreateClubPayload, UpdateClubPayload, UUID>
