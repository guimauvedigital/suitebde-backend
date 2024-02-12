package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class UserInClub(
    val userId: String,
    val clubId: String,
    val user: User? = null,
    val club: Club? = null,
) : IChildModel<String, CreateUserInClubPayload, Unit, String> {

    override val id: String
        get() = userId

    override val parentId: String
        get() = clubId

}
