package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.roles.Permission

@Serializable
data class CreatePermissionNotificationPayload(
    val permission: Permission,
    val title: String,
    val body: String,
    val titleArgs: List<String>? = null,
    val bodyArgs: List<String>? = null,
    val data: Map<String, String>? = null,
)
