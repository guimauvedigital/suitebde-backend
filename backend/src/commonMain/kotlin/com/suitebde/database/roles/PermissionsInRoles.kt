package com.suitebde.database.roles

import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.PermissionInRole
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object PermissionsInRoles : Table() {

    val roleId = uuid("role_id").index()
    val permission = enumerationByName("permission", 255, Permission::class)

    override val primaryKey = PrimaryKey(arrayOf(roleId, permission))

    fun toPermissionInRole(
        row: ResultRow,
    ) = PermissionInRole(
        UUID(row[roleId]),
        row[permission],
    )

}
