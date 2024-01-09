package me.nathanfallet.suitebde.database.roles

import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object PermissionsInRoles : Table() {

    val roleId = varchar("role_id", 32).index()
    val permission = varchar("permission", 255)

    override val primaryKey = PrimaryKey(arrayOf(roleId, permission))

    fun toPermissionInRole(
        row: ResultRow,
    ) = PermissionInRole(
        row[roleId],
        Permission.valueOf(row[permission]),
    )

}
