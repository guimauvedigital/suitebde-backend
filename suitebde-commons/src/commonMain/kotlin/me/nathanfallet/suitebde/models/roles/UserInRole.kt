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
) : IChildModel<String, Unit, Unit, String> {

    override val id = userId
    override val parentId = roleId

}
