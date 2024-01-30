package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class UserInRole(
    val roleId: String,
    val userId: String,
    val role: Role?,
    val user: User?,
) : IChildModel<String, CreateUserInRole, Unit, String> {

    override val id: String
        get() = userId

    override val parentId: String
        get() = roleId

}
