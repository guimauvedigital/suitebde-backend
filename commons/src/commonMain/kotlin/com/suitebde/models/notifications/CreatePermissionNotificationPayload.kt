package com.suitebde.models.notifications

import com.suitebde.models.roles.Permission
import kotlinx.serialization.Serializable

@Serializable
data class CreatePermissionNotificationPayload(
    val permission: Permission,
    val title: String,
    val body: String,
    val titleArgs: List<String>? = null,
    val bodyArgs: List<String>? = null,
    val data: Map<String, String>? = null,
)
