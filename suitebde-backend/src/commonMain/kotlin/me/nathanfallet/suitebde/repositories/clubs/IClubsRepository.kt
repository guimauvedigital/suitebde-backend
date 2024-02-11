package me.nathanfallet.suitebde.repositories.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IClubsRepository : IChildModelSuspendRepository<Club, String, CreateClubPayload, UpdateClubPayload, String> {


}
