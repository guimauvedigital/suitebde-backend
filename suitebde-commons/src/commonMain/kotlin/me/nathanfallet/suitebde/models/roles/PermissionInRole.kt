package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class PermissionInRole(
    @Schema("Id of the role", "123abc")
    val roleId: String,
    @Schema("Permission name", "USERS_UPDATE")
    val permission: Permission,
) : IChildModel<String, CreateDomainInAssociationPayload, Unit, String> {

    override val id = permission.name
    override val parentId = roleId

}
