package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class UserInClub(
    val clubId: String,
    val userId: String,
    val club: Club?,
    val user: User?,
) : IChildModel<String, Unit, Unit, String> {

    override val id = userId
    override val parentId = clubId

}
